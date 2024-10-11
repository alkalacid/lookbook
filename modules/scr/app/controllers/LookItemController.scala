package controllers

import models.dao.entities.LookBookItem
import models.dao.repositories.CrudRepository
import play.api.libs.json.{Json, Reads, Writes}
import play.api.mvc.{Action, AnyContent, Controller}

import scala.language.implicitConversions

abstract class LookItemController[Entity <: LookBookItem, Repository <: CrudRepository[String, Entity]]
  (val repository: Repository) (implicit reads: Reads[Entity], writes: Writes[Entity]) extends Controller {

  def addIdToItem(item: Entity): Entity

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
}
