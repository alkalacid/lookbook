package controllers

import com.google.inject.Inject
import models.services.LookBookServiceImpl
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, Controller}

class LookController @Inject()(val lookBookService: LookBookServiceImpl) extends Controller {

  def generateLook(): Action[AnyContent] = Action{ implicit request =>
    try {
      Ok(Json.toJson(lookBookService.generateLook(request.queryString)))
    } catch {
      case e: Exception => NotFound(e.getMessage)
    }

  }
}
