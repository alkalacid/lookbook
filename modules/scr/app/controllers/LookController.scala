package controllers

import com.google.inject.Inject
import models.dao.repositories.LookRepositoryImpl
import models.dto.{LookDTO, LookFilterDTO}
import models.services.LookBookServiceImpl
import play.api.data.Form
import play.api.data.Forms.{mapping, optional, text}
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, Controller}

class LookController @Inject()(
                                val lookBookService: LookBookServiceImpl,
                                val lookRepository: LookRepositoryImpl
                              ) extends Controller {

  private val generateLookForm = Form(
    mapping(
      "weather" -> text,
      "event" -> text,
      "tailDay" -> text,
      "predefinedItemId" -> optional(text),
      "predefinedItemType" -> optional(text)
    )(LookFilterDTO.apply)(LookFilterDTO.unapply)
  )

  def generateLook: Action[AnyContent] = Action{ implicit request =>
    try {
      val filters: LookFilterDTO = generateLookForm.bindFromRequest.get
      Ok(Json.toJson(lookBookService.generateLook(filters)))
    } catch {
      case e: Exception => NotFound(e.getMessage)
    }
  }

  def add: Action[AnyContent] = Action { implicit rc =>
    try {
      val res = rc.body.asJson.map(_.as[LookDTO]).map(lookBookService.add).get
      Ok(Json.toJson(res))
    } catch {
      case e: Exception => Ok(e.getMessage)
    }
  }

  def update: Action[AnyContent] = Action { implicit rc =>
    try {
      val res = rc.body.asJson.map(_.as[LookDTO]).map(lookBookService.update).get
      Ok(Json.toJson(res))
    } catch {
      case e: Exception => Ok(e.getMessage)
    }
  }

  def delete: Action[AnyContent] = Action { implicit rc =>
    try {
      rc.body.asJson.map(_.as[LookDTO]).map(lookBookService.delete).get
      Ok
    } catch {
      case e: Exception => Ok(e.getMessage)
    }
  }

  def list: Action[AnyContent] = Action{
    try {
      Ok(Json.toJson(lookBookService.get))
    } catch {
      case e: Exception => NotFound(e.getMessage)
    }
  }
}
