package models.services.builders

import models.dao.entities.LookBookItem
import models.dao.repositories.CrudRepository
import models.dto.LookGeneratorDTO
import org.squeryl.dsl.ast.LogicalBoolean

import scala.util.Random

trait Builder[Entity <: LookBookItem, Repository <: CrudRepository[String, Entity]] {

  protected val weatherHeat: String = "heat"
  protected val weatherWinter = "winter"
  protected val weatherRoom = "room"
  protected val weatherWarm = "warm"
  protected val weatherCold = "cold"

  protected val eventRelax = "relax"
  protected val eventCelebrate = "celebrate"
  protected val eventFashion = "celebrate"

  protected val colorBase = "base"
  protected val styleSport = "sport"

  def generate(look: LookGeneratorDTO, filters: Map[String, Seq[String]]): LookGeneratorDTO
  protected def getFilters(look: LookGeneratorDTO, filters: Map[String, Seq[String]]): List[Entity => LogicalBoolean]

  protected def getElementFromDatabase(filters: List[Entity => LogicalBoolean], repository: Repository): Option[Entity] = {
    if (filters.nonEmpty) {
      Random.shuffle(repository.filter(filters = filters)).headOption
    } else {
      Random.shuffle(repository.list()).headOption
    }
  }
}
