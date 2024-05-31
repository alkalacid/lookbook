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
    private def nOpenShoesFilter(): Shoes => LogicalBoolean = _.isOpen === false
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
            checkOut(look)
        }
    }

    override def getFilters(look: Look, filterByWeather: String, filterByEvent: String): List[Shoes => LogicalBoolean] = {
        var filters: List[Shoes => LogicalBoolean] = List.empty
        filterByWeather match {
            case "cold" =>
                filters ::= warmShoesFilter()
            case "warm" =>
                filters ::= noWarmShoesFilter()
                filters ::= nOpenShoesFilter()
            case "heat" =>
                filters ::= noWarmShoesFilter()
            case _ => filters
        }

        if (filterByEvent == "celebrate") {
            filters ::= heelsFilter()
        }

        if (filterByEvent == "relax") {
            filters ::= noHeelsFilter()
        }

        if (filterByEvent == "fashion") {
            val rand: Int = Random.nextInt(2)
            if (rand == 2) {
                filters ::= highFashionabilityFilter()
            } else {
                filters ::= fashionabilityFilter()
            }
        }

        look.length match {
            case "mini" =>
                if (filterByEvent != "celebrate") {
                    filters ::= noHeelsFilter()
                }
            case "max" => filters ::= noHighShoesFilter()
            case "midi" =>
                filters ::= noHighShoesFilter()
                filters ::= heelsFilter()
            case _ => filters
        }

        if(look.hasWeirdElement || filterByEvent == "celebrate" || filterByEvent == "fashion") {
            filters ::= noWeirdFilter()
        }
        if(look.hasColor) {
            filters ::= baseColorFilter()
        }

        filters
    }

    private def checkOut(look: Look): Look = {
        if (look.shoes.get.isWeird) {
            look.hasWeirdElement = true
        }
        if (look.shoes.get.color != "base") {
            look.hasColor = true
        }
        look
    }
}
