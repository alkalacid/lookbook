package controllers

import models.dao.entities.LookBookItem
import models.dao.repositories.CrudRepository
import play.api.libs.json.{Json, Reads, Writes}
import play.api.mvc.{Action, AnyContent, Controller}

import scala.language.implicitConversions

class LookItemController[Entity <: LookBookItem, Repository <: CrudRepository[String, Entity]]
  (val repository: Repository) (implicit reads: Reads[Entity], writes: Writes[Entity]) extends Controller {

  def get: Action[AnyContent] = Action{
    Ok(Json.toJson(repository.list()))
  }

  def add: Action[Entity] = Action(parse.json[Entity](reads)){ rc =>
    Ok(Json.toJson(repository.insert(rc.body))(writes))
  }

  def update: Action[Entity] = Action(parse.json[Entity](reads)){ rc =>
    repository.update(rc.body)
    Ok(Json.toJson(rc.body)(writes))
  }

  def delete: Action[Entity] = Action(parse.json[Entity](reads)){ rc =>
    repository.delete(rc.body)
    Ok(Json.toJson(rc.body)(writes))
  }
}
