package models.services.builders

import com.google.inject.Inject
import models.dao.entities.Bottom
import models.dao.repositories.BottomRepositoryImpl
import models.dto.LookGeneratorDTO
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.dsl.ast.LogicalBoolean

trait BottomClothingBuilder extends ClothingBuilder[Bottom, BottomRepositoryImpl]

class BottomClothingBuilderImpl @Inject()(val bottomRepository: BottomRepositoryImpl) extends BottomClothingBuilder {

  val builderName: String = "bottom"

  private def noMidFilter(): Bottom => LogicalBoolean = _.length <> lengthMid
  private def noMidiFilter(): Bottom => LogicalBoolean = _.length <> lengthMidi
  private def noMaxFilter(): Bottom => LogicalBoolean = _.length <> lengthMax

  override def generate(look: LookGeneratorDTO): LookGeneratorDTO = {

    if (look.hasDress) {
      look
    } else {

      val filters: List[Bottom => LogicalBoolean] = getFilters(look)
      val bottom: Option[Bottom] = getElementFromDatabase(filters, bottomRepository)

      if(bottom.isEmpty) {
        throw new Exception("No bottom was found")
      } else {
        look.bottom = bottom
      }

      look.length = look.bottom.get.length
      checkOut(look, look.bottom)
    }
  }

  override def getFilters(look: LookGeneratorDTO): List[Bottom => LogicalBoolean] = {

    val filters = if (look.weather == weatherHeat) {
      noMaxFilter() :: checkIn(look)
    } else {
      checkIn(look)
    }

    val filterByEvent = look.event
    List(getFiltersByLength(look), getFiltersByEvent(filterByEvent), getFilterByRelaxEvent(filterByEvent), filters).flatten
  }

  private def getFiltersByLength(look: LookGeneratorDTO): List[Bottom => LogicalBoolean] = {

    if (look.top.get.length == lengthHip || (
      look.coating.isDefined && look.coating.get.length == lengthHip
      )) {
      List(noMidFilter(), noMidiFilter())
    } else List.empty

  }
}