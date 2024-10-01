package models.services.builders

import com.google.inject.Inject
import models.dao.entities.Top
import models.dao.repositories.TopRepositoryImpl
import models.dto.LookGeneratorDTO
import org.squeryl.dsl.ast.LogicalBoolean
import org.squeryl.PrimitiveTypeMode._

import scala.util.Random

trait CoatingClothingBuilder extends ClothingBuilder[Top, TopRepositoryImpl]

class CoatingClothingBuilderImpl @Inject()(val topRepository: TopRepositoryImpl) extends CoatingClothingBuilder {

  val builderName: String = "coating"

  private val topNoCoating = "noCoating"

  private def coatingFilter(): Top => LogicalBoolean = _. isCoating <> topNoCoating
  private def noDressFilter(): Top => LogicalBoolean = _.isDress === false
  private def dressFilter(): Top => LogicalBoolean = _.isDress === true

  override def generate(look: LookGeneratorDTO): LookGeneratorDTO = {
    if (look.weather == weatherHeat) {
      look
    } else if (look.top.get.isSleeve && Random.nextInt(4) != 1) {
      look
    } else {
      val filters = getFilters(look)
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

  override def getFilters(look: LookGeneratorDTO): List[Top => LogicalBoolean] = {
    if (look.hasDress) {
      List(noDressFilter() :: coatingFilter() :: checkIn(look), getFiltersByEvent(look.event)).flatten
    } else if (look.top.get.isSleeve) {
      List(dressFilter() :: coatingFilter() :: checkIn(look), getFiltersByEvent(look.event)).flatten
    } else {
      List(coatingFilter() :: checkIn(look), getFiltersByEvent(look.event)).flatten
    }

  }
}
