package models.services.builders

import com.google.inject.Inject
import models.dao.entities.Top
import models.dao.repositories.TopRepositoryImpl
import models.dto.Look
import org.squeryl.dsl.ast.LogicalBoolean
import org.squeryl.PrimitiveTypeMode._
import scala.util.Random

trait TopBuilder extends Builder[String, Top]

class TopBuilderImpl @Inject()(val topRepository: TopRepositoryImpl) extends TopBuilder {

    private def noSleevesFilter(): Top => LogicalBoolean = _.isSleeve === false
    private def noCoatingFilter(): Top => LogicalBoolean = _.isCoating <> "onlyCoating"

    override def generate(look: Look, filterByWeather: String, filterByEvent: String): Look = {
        val filters = getFilters(look, filterByWeather, filterByEvent)
        look.top = Some(Random.shuffle(topRepository.filter(filters=filters)).head)
        checkOut(look)
    }

    override def getFilters(look: Look, filterByWeather: String, filterByEvent: String): List[Top => LogicalBoolean] = {
        var filters: List[Top => LogicalBoolean] = List.empty
        filters ::= noCoatingFilter()

        if (filterByWeather == "heat") {
            filters ::= noSleevesFilter()
        }

        if ((filterByEvent == "celebrate") || filterByEvent == "fashion") {
            filters ::= noWeirdFilter()
        }

        if (filterByEvent == "relax") {
            val rand: Int = Random.nextInt(2)
            if (rand == 2) {
                filters ::= sportStyleFilter()
            }
        }

        if (filterByEvent == "fashion") {
            val rand: Int = Random.nextInt(2)
            if (rand == 2) {
                filters ::= highFashionabilityFilter()
            } else {
                filters ::= fashionabilityFilter()
            }
        }

        filters
    }

    private def checkOut(look: Look): Look = {
        if (look.top.get.isWeird) {
            look.hasWeirdElement = true
        }
        if (look.top.get.color != "base") {
            look.hasColor = true
        }
        look
    }
}
