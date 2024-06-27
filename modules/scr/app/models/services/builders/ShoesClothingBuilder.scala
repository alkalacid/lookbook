package models.services.builders

import com.google.inject.Inject
import models.dao.entities.Shoes
import models.dao.repositories.ShoesRepositoryImpl
import models.dto.LookGeneratorDTO
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.dsl.ast.LogicalBoolean

trait ShoesClothingBuilder extends ClothingBuilder[Shoes, ShoesRepositoryImpl]

class ShoesClothingBuilderImpl @Inject()(val shoesRepository: ShoesRepositoryImpl) extends ShoesClothingBuilder {

  private def warmShoesFilter(): Shoes => LogicalBoolean = _.isWarm === true
  private def noWarmShoesFilter(): Shoes => LogicalBoolean = _.isWarm === false
  private def noOpenShoesFilter(): Shoes => LogicalBoolean = _.isOpen === false
  private def noHeelsFilter(): Shoes => LogicalBoolean = _.isHeel === false
  private def heelsFilter(): Shoes => LogicalBoolean = _.isHeel === true
  private def noHighShoesFilter(): Shoes => LogicalBoolean = _.isHigh === false

  override def generate(look: LookGeneratorDTO, queryFilters: Map[String, Seq[String]]): LookGeneratorDTO = {

    if (queryFilters("weather").head == weatherWinter || queryFilters("weather").head == weatherRoom) {
      look
    } else {

      val filters = getFilters(look, queryFilters: Map[String, Seq[String]])
      val shoes: Option[Shoes] = getElementFromDatabase(filters, shoesRepository)

      if(shoes.isEmpty) {
        throw new Exception("No shoes was found")
      } else {
        look.shoes = shoes
      }
      checkOut(look, look.shoes)
    }
  }

  override def getFilters(look: LookGeneratorDTO, queryFilters: Map[String, Seq[String]]): List[Shoes => LogicalBoolean] = {
    List(checkIn(look),
      getFiltersByWeather(queryFilters("weather").head),
      getFiltersByEvent(queryFilters("event").head),
      getFiltersByLength(look.length, queryFilters("event").head),
      getHeelsFiltersByEvent(queryFilters("event").head)).flatten
  }

  private def getFiltersByWeather(weather: String): List[Shoes => LogicalBoolean] = weather match {
    case this.weatherCold => List(warmShoesFilter())
    case this.weatherWarm => List(noWarmShoesFilter(), noOpenShoesFilter())
    case this.weatherHeat => List(noWarmShoesFilter())
    case _ => List()
  }

  private def getFiltersByLength(length: String, event: String): List[Shoes => LogicalBoolean] = length match {
    case this.lengthMini =>
      if (event != eventCelebrate) List(noHeelsFilter()) else List()
    case this.lengthMax => List(noHighShoesFilter())
    case this.lengthMidi => if (event != eventRelax) List(noHighShoesFilter(), heelsFilter()) else List(noHighShoesFilter())
    case _ => List()
  }

  private def getHeelsFiltersByEvent(event: String): List[Shoes => LogicalBoolean] = event match {
    case this.eventCelebrate => List(heelsFilter())
    case this.eventRelax => List(noHeelsFilter())
    case _ => List()
  }
}
