package models.services.builders

import com.google.inject.Inject
import models.dao.entities.Top
import models.dao.repositories.TopRepositoryImpl
import models.dto.LookGeneratorDTO
import org.squeryl.dsl.ast.LogicalBoolean
import org.squeryl.PrimitiveTypeMode._

import scala.util.Random

trait CoatingClothingBuilder extends ClothingBuilder[Top, TopRepositoryImpl]

class CoatingClothingBuilderImpl @Inject()(val repository: TopRepositoryImpl) extends CoatingClothingBuilder {

  val builderName: String = "coating"

  private val topNoCoating = "noCoating"

  private def coatingFilter(): Top => LogicalBoolean = _. isCoating <> topNoCoating
  private def noDressFilter(): Top => LogicalBoolean = _.isDress === false
  private def dressFilter(): Top => LogicalBoolean = _.isDress === true
  private def noHipLengthFilter(): Top => LogicalBoolean = _.length <> lengthHip

  override def generate(look: LookGeneratorDTO, itemId: String): LookGeneratorDTO = {
    if (look.weather == weatherHeat && itemId.isEmpty) {
      look
    } else if (look.hasSleeves && Random.nextInt(5) != 1 && itemId.isEmpty) {
      look
    } else {
      val coating: Option[Top] = getItem(look, itemId, repository)

      if(coating.isEmpty) {
        throw new Exception("No coating was found")
      } else {
        look.coating = coating
      }

      if(coating.get.isDress) {
        look.hasDress = true
        if(look.length.isEmpty) {
          look.length = coating.get.length
        }
      }

      checkOut(look, look.coating)
    }
  }

  override def getFilters(look: LookGeneratorDTO): List[Top => LogicalBoolean] = {
    if (look.hasDress || look.bottom.isDefined) {
      List(noDressFilter() :: coatingFilter() :: checkIn(look), getFiltersByEvent(look.event)).flatten
    } else if (look.hasSleeves) {
      List(dressFilter() :: coatingFilter() :: checkIn(look), getFiltersByEvent(look.event)).flatten
    } else if (look.length.nonEmpty && look.length == lengthMidi) {
      List(noHipLengthFilter() :: coatingFilter() :: checkIn(look), getFiltersByEvent(look.event)).flatten
    } else {
      List(coatingFilter() :: checkIn(look), getFiltersByEvent(look.event)).flatten
    }

  }
}
