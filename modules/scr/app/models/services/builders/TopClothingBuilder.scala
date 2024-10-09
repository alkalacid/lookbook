package models.services.builders

import com.google.inject.Inject
import models.dao.entities.Top
import models.dao.repositories.TopRepositoryImpl
import models.dto.LookGeneratorDTO
import org.squeryl.dsl.ast.LogicalBoolean
import org.squeryl.PrimitiveTypeMode._

trait TopClothingBuilder extends ClothingBuilder[Top, TopRepositoryImpl]

class TopClothingBuilderImpl @Inject()(val repository: TopRepositoryImpl) extends TopClothingBuilder {

  override val builderName: String = "top"

  private val topOnlyCoating = "onlyCoating"

  private def noSleevesFilter(): Top => LogicalBoolean = _.isSleeve === false
  private def noCoatingFilter(): Top => LogicalBoolean = _.isCoating <> topOnlyCoating
  private def noDressFilter(): Top => LogicalBoolean = _.isDress === false

  override def generate(look: LookGeneratorDTO, itemId: String): LookGeneratorDTO = {
    val top: Option[Top] = getItem(look, itemId, repository)

    if(top.isEmpty) {
      throw new Exception("No top was found")
    } else {
      look.top = top
      if(look.length.isEmpty) {
        look.length = top.get.length
      }
      if(top.get.isDress) {
        look.hasDress = true
      }
      if(top.get.isSleeve) {
        look.hasSleeves = true
      }
      checkOut(look, look.top)
    }
  }

  override def getFilters(look: LookGeneratorDTO): List[Top => LogicalBoolean] = {

    val filters = if (look.weather == weatherHeat) {
      List(noCoatingFilter(), noSleevesFilter())
    } else if (look.bottom.isDefined) {
      List(noCoatingFilter(), noDressFilter())
    } else List(noCoatingFilter())

    val filterByEvent = look.event
    List(getFiltersByEvent(filterByEvent), getFilterByRelaxEvent(filterByEvent), filters, checkIn(look)).flatten

  }
}
