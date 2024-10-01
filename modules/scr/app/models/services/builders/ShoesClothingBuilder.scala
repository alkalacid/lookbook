package models.services.builders

import com.google.inject.Inject
import models.dao.entities.Shoes
import models.dao.repositories.ShoesRepositoryImpl
import models.dto.LookGeneratorDTO
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.dsl.ast.LogicalBoolean

trait ShoesClothingBuilder extends ClothingBuilder[Shoes, ShoesRepositoryImpl]

class ShoesClothingBuilderImpl @Inject()(val shoesRepository: ShoesRepositoryImpl) extends ShoesClothingBuilder {

  val builderName: String = "shoes"

  private def warmShoesFilter(): Shoes => LogicalBoolean = _.isWarm === true
  private def noWarmShoesFilter(): Shoes => LogicalBoolean = _.isWarm === false
  private def noOpenShoesFilter(): Shoes => LogicalBoolean = _.isOpen === false
  private def noHeelsFilter(): Shoes => LogicalBoolean = _.isHeel === false
  private def heelsFilter(): Shoes => LogicalBoolean = _.isHeel === true
  private def noHighShoesFilter(): Shoes => LogicalBoolean = _.isHigh === false

  override def generate(look: LookGeneratorDTO): LookGeneratorDTO = {

    if (look.weather == weatherWinter || look.weather == weatherRoom) {
      look
    } else {

      val filters = getFilters(look)
      val shoes: Option[Shoes] = getElementFromDatabase(filters, shoesRepository)

      if(shoes.isEmpty) {
        throw new Exception("No shoes was found")
      } else {
        look.shoes = shoes
      }
      checkOut(look, look.shoes)
    }
  }

  override def getFilters(look: LookGeneratorDTO): List[Shoes => LogicalBoolean] = {
    List(checkIn(look),
      getFiltersByWeather(look.weather),
      getFiltersByEvent(look.event),
      getFiltersByLength(look),
      getHeelsFiltersByEvent(look.weather)).flatten
  }

  private def getFiltersByWeather(weather: String): List[Shoes => LogicalBoolean] = weather match {
    case this.weatherCold => List(warmShoesFilter())
    case this.weatherWarm => List(noWarmShoesFilter(), noOpenShoesFilter())
    case this.weatherHeat => List(noWarmShoesFilter())
    case _ => List()
  }

  private def getFiltersByLength(look: LookGeneratorDTO): List[Shoes => LogicalBoolean] = look.length match {
    case this.lengthMini =>
      if (look.event != eventCelebrate) List(noHeelsFilter()) else List()
    case this.lengthMax => List(noHighShoesFilter())
    case this.lengthMidi => if (look.event != eventRelax) List(noHighShoesFilter(), heelsFilter()) else List(noHighShoesFilter())
    case _ => List()
  }

  private def getHeelsFiltersByEvent(event: String): List[Shoes => LogicalBoolean] = event match {
    case this.eventCelebrate => List(heelsFilter())
    case this.eventRelax => List(noHeelsFilter())
    case _ => List()
  }
}
