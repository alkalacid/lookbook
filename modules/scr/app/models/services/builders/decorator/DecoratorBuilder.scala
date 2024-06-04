package models.services.builders.decorator

import models.dao.entities.DecoratorItem
import models.dao.repositories.CrudRepository
import models.dto.Look
import org.squeryl.dsl.ast.LogicalBoolean
import org.squeryl.PrimitiveTypeMode._

import scala.util.Random

trait DecoratorBuilder[Entity <: DecoratorItem, Repository <: CrudRepository[String, Entity]] {
  private def noWeirdFilter(): DecoratorItem => LogicalBoolean = _.isWeird === false

  def generate(look: Look, filters: Map[String, Seq[String]]): Look
  protected def getFilters(look: Look, filters: Map[String, Seq[String]]): List[Entity => LogicalBoolean] = {
    checkIn(look)
  }

  protected def checkIn(look: Look): List[DecoratorItem => LogicalBoolean] = {
    if (look.hasWeirdElement) List(noWeirdFilter()) else List()
  }

  protected def checkOut(look: Look, lookPiece: Option[DecoratorItem]): Look = lookPiece match {
    case Some(x) =>
      if (x.isWeird) look.hasWeirdElement = true
      look
    case _ => look
  }

  protected def getElementFromDatabase(filters: List[Entity => LogicalBoolean])
                                      (implicit repository: Repository): Option[Entity] = {
    if (filters.nonEmpty) {
      Random.shuffle(repository.filter(filters = filters)).headOption
    } else {
      Random.shuffle(repository.list()).headOption
    }
  }
}