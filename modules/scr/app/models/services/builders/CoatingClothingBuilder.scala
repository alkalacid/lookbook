package models.services.builders

import com.google.inject.Inject
import models.dao.entities.Top
import models.dao.repositories.TopRepositoryImpl
import models.dto.LookGeneratorDTO
import org.squeryl.dsl.ast.LogicalBoolean
import org.squeryl.PrimitiveTypeMode._

trait CoatingClothingBuilder extends ClothingBuilder[Top, TopRepositoryImpl]

class CoatingClothingBuilderImpl @Inject()(val topRepository: TopRepositoryImpl) extends CoatingClothingBuilder {

  private val topNoCoating = "noCoating"

  private def coatingFilter(): Top => LogicalBoolean = _. isCoating <> topNoCoating
  private def noDressFilter(): Top => LogicalBoolean = _.isDress === false

  override def generate(look: LookGeneratorDTO, queryFilters: Map[String, Seq[String]]): LookGeneratorDTO = {
    if (look.top.get.isSleeve || queryFilters("weather").head == weatherHeat) {
      look
    } else {
      val filters = getFilters(look, queryFilters: Map[String, Seq[String]])
      val coating: Option[Top] = getElementFromDatabase(filters, topRepository)

      if(coating.isEmpty) {
        throw new Exception("No coating was found")
      } else {
        look.coating = coating
      }

      if(coating.get.isDress) {
        look.hasDress = true
        look.length = coating.get.length
      }

      checkOut(look, look.coating)
    }
  }

  override def getFilters(look: LookGeneratorDTO, queryFilters: Map[String, Seq[String]]): List[Top => LogicalBoolean] = {
    if (look.hasDress) {
      List(noDressFilter() :: coatingFilter() :: checkIn(look), getFiltersByEvent(queryFilters("event").head)).flatten
    } else {
      List(coatingFilter() :: checkIn(look), getFiltersByEvent(queryFilters("event").head)).flatten
    }

  }
}
