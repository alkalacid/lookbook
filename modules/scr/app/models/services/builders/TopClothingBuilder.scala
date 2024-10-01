package models.services.builders

import com.google.inject.Inject
import models.dao.entities.Top
import models.dao.repositories.TopRepositoryImpl
import models.dto.LookGeneratorDTO
import org.squeryl.dsl.ast.LogicalBoolean
import org.squeryl.PrimitiveTypeMode._

trait TopClothingBuilder extends ClothingBuilder[Top, TopRepositoryImpl]

class TopClothingBuilderImpl @Inject()(val topRepository: TopRepositoryImpl) extends TopClothingBuilder {

  private val topOnlyCoating = "onlyCoating"

  private def noSleevesFilter(): Top => LogicalBoolean = _.isSleeve === false
  private def noCoatingFilter(): Top => LogicalBoolean = _.isCoating <> topOnlyCoating

  override def generate(look: LookGeneratorDTO, queryFilters: Map[String, Seq[String]]): LookGeneratorDTO = {
    val filters = getFilters(look, queryFilters)
    val top: Option[Top] = getElementFromDatabase(filters, topRepository)

    if(top.isEmpty) {
      throw new Exception("No top was found")
    } else {
      look.top = top
      look.length = top.get.length
      if(top.get.isDress) {
        look.hasDress = true
      }
      checkOut(look, look.top)
    }
  }

  override def getFilters(look: LookGeneratorDTO, queryFilters: Map[String, Seq[String]]): List[Top => LogicalBoolean] = {

    val filters = if (queryFilters("weather").head == weatherHeat) {
      List(noCoatingFilter(), noSleevesFilter())
    } else {
      List(noCoatingFilter())
    }

    val filterByEvent = queryFilters("event").head
    List(getFiltersByEvent(filterByEvent), getFilterByRelaxEvent(filterByEvent), filters).flatten

  }
}
