package models.services.builders.decorator

import models.dao.entities.LookBookItem
import models.dao.repositories.CrudRepository
import models.dto.LookGeneratorDTO
import models.services.builders.Builder
import org.squeryl.dsl.ast.LogicalBoolean
import org.squeryl.PrimitiveTypeMode._

trait DecoratorBuilder[Entity <: LookBookItem, Repository <: CrudRepository[String, Entity]] extends Builder[Entity, Repository] {
  private def noWeirdFilter(): LookBookItem => LogicalBoolean = _.isWeird === false

  protected def getFilters(look: LookGeneratorDTO, filters: Map[String, Seq[String]]): List[Entity => LogicalBoolean] = {
    checkIn(look)
  }

  protected def checkIn(look: LookGeneratorDTO): List[LookBookItem => LogicalBoolean] = {
    if (look.hasWeirdElement) List(noWeirdFilter()) else List()
  }

  protected def checkOut(look: LookGeneratorDTO, lookPiece: Option[LookBookItem]): LookGeneratorDTO = lookPiece match {
    case Some(x) =>
      if (x.isWeird) look.hasWeirdElement = true
      look
    case _ => look
  }
}