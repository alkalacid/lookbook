package controllers

import com.google.inject.Inject
import models.dao.entities.Look
import models.dao.repositories.LookRepositoryImpl
import models.dto.LookDTO
import models.services.LookBookServiceImpl
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, Controller}

class LookController @Inject()(
                                val lookBookService: LookBookServiceImpl,
                                val lookRepository: LookRepositoryImpl
                              ) extends Controller {

  def generateLook(
                  weather: String = "any",
                  event: String = "any",
                  tailDay: String = "low"
                  ): Action[AnyContent] = Action{ implicit request =>
    try {
      Ok(Json.toJson(lookBookService.generateLook(weather, event, tailDay)))
    } catch {
      case e: Exception => NotFound(e.getMessage)
    }
  }

  def add: Action[AnyContent] = Action{ implicit rc =>
    try {
      val res = rc.body.asJson.map(_.as[LookDTO]).map(lookBookService.add).get
      Ok(Json.toJson(res))
    } catch {
      case e: Exception => Ok(e.getMessage)
    }
  }

  def update: Action[Look] = Action(parse.json[Look]){ rc =>
    lookRepository.update(rc.body)
    Ok(Json.toJson(rc.body))
  }

  def delete: Action[Look] = Action(parse.json[Look]){ rc =>
    lookRepository.delete(rc.body)
    Ok(Json.toJson(rc.body))
  }

  def list: Action[AnyContent] = Action{
    try {
      Ok(Json.toJson(lookBookService.get))
    } catch {
      case e: Exception => NotFound(e.getMessage)
    }
  }
}
