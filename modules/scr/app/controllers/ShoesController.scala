package controllers

import com.google.inject.Inject
import models.dao.entities.Shoes
import models.dao.repositories.ShoesRepositoryImpl
import play.api.libs.json.Format.GenericFormat
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

class ShoesController @Inject()(val shoesRepository: ShoesRepositoryImpl) extends Controller {

  def get = Action{
    Ok(Json.toJson(shoesRepository.list()))
  }

  def add: Action[Shoes] = Action(parse.json[Shoes]){ rc =>
    Ok(Json.toJson(shoesRepository.insert(rc.body)))
  }

  def update: Action[Shoes] = Action(parse.json[Shoes]){ rc =>
    shoesRepository.update(rc.body)
    Ok(Json.toJson(rc.body))
  }

  def delete: Action[Shoes] = Action(parse.json[Shoes]){ rc =>
    shoesRepository.delete(rc.body)
    Ok(Json.toJson(rc.body))
  }
}
