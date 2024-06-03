package models.services.builders

import com.google.inject.Inject
import models.dao.entities.Shoes
import models.dao.repositories.ShoesRepositoryImpl
import models.dto.Look
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.dsl.ast.LogicalBoolean
import scala.util.Random

trait ShoesBuilder extends Builder[String, Shoes]

class ShoesBuilderImpl @Inject()(val shoesRepository: ShoesRepositoryImpl) extends ShoesBuilder {

    private def warmShoesFilter(): Shoes => LogicalBoolean = _.isWarm === true
    private def noWarmShoesFilter(): Shoes => LogicalBoolean = _.isWarm === false
    private def noOpenShoesFilter(): Shoes => LogicalBoolean = _.isOpen === false
    private def noHeelsFilter(): Shoes => LogicalBoolean = _.isHeel === false
    private def heelsFilter(): Shoes => LogicalBoolean = _.isHeel === true
    private def noHighShoesFilter(): Shoes => LogicalBoolean = _.isHigh === false

    override def generate(look: Look, filterByWeather: String, filterByEvent: String): Look = {
        if (filterByWeather == "winter" || filterByWeather == "room") {
            look
        } else {
            val filters = getFilters(look, filterByWeather, filterByEvent)

            if (filters.nonEmpty) {
                look.shoes = Some(Random.shuffle(shoesRepository.filter(filters = filters)).head)
            } else {
                look.shoes =Some(Random.shuffle(shoesRepository.list()).head)
            }
            checkOut(look, look.shoes)
        }
    }

    override def getFilters(look: Look, filterByWeather: String, filterByEvent: String): List[Shoes => LogicalBoolean] = {
        var filters: List[Shoes => LogicalBoolean] = checkIn(look) ::: getFiltersByWeather(filterByWeather)

        if (filterByEvent == "celebrate") {
            filters ::= heelsFilter()
        }

        if (filterByEvent == "relax") {
            filters ::= noHeelsFilter()
        }

        getFiltersByEvent(filterByEvent) ::: getFiltersByLength(look.length, filterByEvent)::: filters
    }

    private def getFiltersByWeather(weather: String): List[Shoes => LogicalBoolean] = weather match {
        case "cold" => List(warmShoesFilter())
        case "warm" => List(noWarmShoesFilter(), noOpenShoesFilter())
        case "heat" => List(noWarmShoesFilter())
        case _ => List()
    }

    private def getFiltersByLength(length: String, event: String): List[Shoes => LogicalBoolean] = length match {
        case "mini" =>
            if (event != "celebrate") List(noHeelsFilter()) else List()
        case "max" => List(noHighShoesFilter())
        case "midi" => if (event != "relax") List(noHighShoesFilter(), heelsFilter()) else List(noHighShoesFilter())
        case _ => List()
    }
}
