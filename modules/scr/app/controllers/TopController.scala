package controllers

import com.google.inject.Inject
import models.dao.entities.Top
import models.dao.repositories.TopRepositoryImpl
import play.api.libs.json.Format.GenericFormat
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

class TopController @Inject()(val topRepository: TopRepositoryImpl) extends Controller {

  def get = Action{
    Ok(Json.toJson(topRepository.list()))
  }

  def add: Action[Top] = Action(parse.json[Top]){ rc =>
    Ok(Json.toJson(topRepository.insert(rc.body)))
  }

  def update: Action[Top] = Action(parse.json[Top]){ rc =>
    topRepository.update(rc.body)
    Ok(Json.toJson(rc.body))
  }

  def delete: Action[Top] = Action(parse.json[Top]){ rc =>
    topRepository.delete(rc.body)
    Ok(Json.toJson(rc.body))
  }
}
