package models.services.builders

import models.dao.entities.LookBookItem
import models.dao.repositories.CrudRepository
import models.dto.LookGeneratorDTO
import org.squeryl.dsl.ast.LogicalBoolean

import scala.util.Random

trait Builder[Entity <: LookBookItem, Repository <: CrudRepository[String, Entity]] extends AbstractBuilder{

  protected def getFilters(look: LookGeneratorDTO): List[Entity => LogicalBoolean]

  protected def getElementFromDatabase(filters: List[Entity => LogicalBoolean], repository: Repository): Option[Entity] = {
    if (filters.nonEmpty) {
      Random.shuffle(repository.filter(filters = filters)).headOption
    } else {
      Random.shuffle(repository.list()).headOption
    }
  }
}
