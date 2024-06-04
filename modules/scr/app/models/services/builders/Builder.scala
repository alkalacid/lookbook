package models.services.builders

import models.dao.entities.ClothingItem
import models.dao.repositories.CrudRepository
import models.dto.Look
import org.squeryl.KeyedEntity
import org.squeryl.dsl.ast.LogicalBoolean
import org.squeryl.PrimitiveTypeMode._

import scala.util.Random

trait Builder[K, Entity <: KeyedEntity[K], Repository <: CrudRepository[K, Entity]] {
  private def noWeirdFilter(): ClothingItem => LogicalBoolean = _.isWeird === false
  private def baseColorFilter(): ClothingItem => LogicalBoolean = _.color === "base"
  private def sportStyleFilter(): ClothingItem => LogicalBoolean = _.style === "sport"
  private def highFashionabilityFilter(): ClothingItem => LogicalBoolean = _.fashionability gte 50
  private def fashionabilityFilter(): ClothingItem => LogicalBoolean = _.fashionability gt 0

  def generate(look: Look, filterByWeather: String, filterByEvent: String): Look
  protected def getFilters(look: Look, filterByWeather: String, filterByEvent: String): List[Entity => LogicalBoolean]

  protected def checkIn(look: Look): List[ClothingItem => LogicalBoolean] = {
    var filters: List[ClothingItem => LogicalBoolean] = List.empty
    if (look.hasWeirdElement) filters ::= noWeirdFilter()
    if (look.hasColor) filters ::= baseColorFilter()
    filters
  }

  protected def checkOut(look: Look, lookPiece: Option[ClothingItem]): Look = lookPiece match {
    case Some(x) =>
      if (x.isWeird) look.hasWeirdElement = true
      if (x.color != "base") look.hasColor = true
      look
    case _ => look
  }

  protected def getFiltersByEvent(event: String): List[ClothingItem => LogicalBoolean] = {

    var filters: List[ClothingItem => LogicalBoolean] = List.empty

    if ((event == "celebrate") || event == "fashion") {
      filters ::= noWeirdFilter()
    }

    if (event == "fashion" && Random.nextInt(3) == 2) {
      filters ::= highFashionabilityFilter()
    } else {
      filters ::= fashionabilityFilter()
    }

    filters
  }

  protected def getFilterByRelaxEvent(event: String): List[ClothingItem => LogicalBoolean] = {
    if (event == "relax" && Random.nextInt(3) == 2) {
      List(sportStyleFilter())
    } else {
      List.empty
    }
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
