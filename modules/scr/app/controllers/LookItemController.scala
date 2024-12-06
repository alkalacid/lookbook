package controllers

import models.dao.entities.LookBookItem
import models.dao.repositories.CrudRepository
import org.squeryl.PrimitiveTypeMode.transaction
import play.api.libs.Files
import play.api.libs.Files.TemporaryFile
import play.api.libs.json.{Json, Reads, Writes}
import play.api.mvc.{Action, AnyContent, Controller, MultipartFormData}
import utils.Constants
import utils.ItemNotFoundException

import java.io.{File, FileInputStream}
import java.nio.file.Paths
import java.util.{Base64, UUID}
import scala.language.implicitConversions

abstract class LookItemController[Entity <: LookBookItem, Repository <: CrudRepository[String, Entity]]
  (val repository: Repository) (implicit reads: Reads[Entity], writes: Writes[Entity]) extends Controller {

  def addIdToItem(item: Entity): Entity

  def setImageName(item: Entity, newImageName: String): Entity

  def get: Action[AnyContent] = Action{
    Ok(Json.toJson(repository.list()))
  }

  def add: Action[Entity] = Action(parse.json[Entity](reads)){ rc =>
    try {
      val entity = addIdToItem(rc.body)
      Ok(Json.toJson(repository.insert(entity))(writes))
    } catch {
      case e: Exception => NotFound(e.getMessage)
    }
  }

  def update: Action[Entity] = Action(parse.json[Entity](reads)){ rc =>
    try {
      repository.update(rc.body)
      Ok(Json.toJson(rc.body)(writes))
    } catch {
      case e: Exception => NotFound(e.getMessage)
    }
  }

  def delete: Action[Entity] = Action(parse.json[Entity](reads)){ rc =>
    try {
      repository.delete(rc.body)
      Ok
    } catch {
      case e: Exception => NotFound(e.getMessage)
    }
  }

  def uploadOrDeleteImage(entityId: String): Action[MultipartFormData[Files.TemporaryFile]] = Action(parse.multipartFormData) {
    request =>
      request.body
        .file("file")
        .map { image =>

          val extension = image.filename.split("\\.").last

          if (!Constants.allowedExtensions.contains(extension)) {
            BadRequest(s"Invalid file extension: $extension. Allowed extensions: jpg, jpeg, png, gif")
          } else {
            try {
              transaction {
                Ok(addImage(extension, entityId, image.ref))
              }
            } catch {
              case exception: ItemNotFoundException => NotFound(exception.getMessage)
            }
          }

        }.getOrElse {
          transaction {
            Ok(deleteImage(entityId))
          }
        }
  }

  def getBase64Image(entityId: String): Action[AnyContent] = Action {

    val imageName = repository.find(entityId) match {
      case Some(entity) => entity.image
      case None => throw new ItemNotFoundException("Item not found")
    }

    val file = Paths.get(Constants.projectRoot + imageName).toFile
    if (file.exists) {
      val inputStream = new FileInputStream(file)
      val bytesArray = new Array[Byte](file.length.toInt)

      inputStream.read(bytesArray)
      inputStream.close()

      val fileExtension = file.getName.split("\\.").last
      val result = "data:image/" + fileExtension + ";base64," + Base64.getEncoder.encodeToString(bytesArray)
      Ok(result)

    } else NotFound("File not found")
  }

  private def addImage(extension: String, entityId: String, image: TemporaryFile): String = {
    val generatedFilename = "/upload/" + UUID.randomUUID.toString.replaceAll("-", "") + "." + extension

    repository.find(entityId) match {
      case Some(entity) => repository.update(setImageName(entity, generatedFilename))
      case None => throw new ItemNotFoundException("Item not found")
    }

    val path = Paths.get(Constants.projectRoot + generatedFilename).toFile
    image.moveTo(path, replace = false)

    generatedFilename
  }

  private def deleteImage(entityId: String): String = {
    val imageName = repository.find(entityId) match {
      case Some(entity) =>
        repository.update(setImageName(entity, ""))
        entity.image
      case None => throw new ItemNotFoundException("Item not found")
    }
    val path = Paths.get(Constants.projectRoot + imageName).toFile
    if (path.delete()) {
      "Image deleted"
    } else {
      "Image already empty"
    }
  }
}
