package controllers

import com.google.inject.Inject
import models.dao.entities.Bottom
import models.dao.repositories.BottomRepositoryImpl
import play.api.libs.json.Format.GenericFormat
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

class BottomController @Inject()(val bottomRepository: BottomRepositoryImpl) extends Controller {

  def get = Action{
    Ok(Json.toJson(bottomRepository.list()))
  }

  def add: Action[Bottom] = Action(parse.json[Bottom]){ rc =>
    Ok(Json.toJson(bottomRepository.insert(rc.body)))
  }

  def update: Action[Bottom] = Action(parse.json[Bottom]){ rc =>
    bottomRepository.update(rc.body)
    Ok(Json.toJson(rc.body))
  }

  def delete: Action[Bottom] = Action(parse.json[Bottom]){ rc =>
    bottomRepository.delete(rc.body)
    Ok(Json.toJson(rc.body))
  }
}
