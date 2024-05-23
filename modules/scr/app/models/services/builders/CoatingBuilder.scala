package models.services.builders

import com.google.inject.Inject
import models.dao.entities.Top
import models.dao.repositories.TopRepositoryImpl
import models.dto.Look
import org.squeryl.dsl.ast.LogicalBoolean
import org.squeryl.PrimitiveTypeMode._
import scala.util.Random

trait CoatingBuilder extends Builder[String, Top]

class CoatingBuilderImpl @Inject()(val topRepository: TopRepositoryImpl) extends CoatingBuilder {

    private def coatingFilter(): Top => LogicalBoolean = _. isCoating <> "noCoating"
    private def noWeirdTopFilter(): Top => LogicalBoolean = _.isWeird === false
    private def baseColorTopFilter(): Top => LogicalBoolean = _.color === "base"
    private def highFashionabilityFilter(): Top => LogicalBoolean = _.fashionability gte 50
    private def fashionabilityFilter(): Top => LogicalBoolean = _.fashionability gt 0

    override def generate(look: Look, filterByWeather: String, filterByEvent: String): Look = {
        if (look.top.get.isSleeve || filterByWeather == "heat") {
            look
        } else {
            val filters = getFilters(look, filterByWeather, filterByEvent)
            look.coating = Some(Random.shuffle(topRepository.filter(filters=filters)).head)
            checkOut(look)
        }

    }

    override def getFilters(look: Look, filterByWeather: String, filterByEvent: String): List[Top => LogicalBoolean] = {
        var filters: List[Top => LogicalBoolean] = List.empty
        filters ::= coatingFilter()

        if (look.hasWeirdElement || filterByEvent == "celebrate" || filterByEvent == "fashion") {
            filters ::= noWeirdTopFilter()
        }
        if (filterByEvent == "fashion") {
            val rand: Int = Random.nextInt(2)
            if (rand == 2) {
                filters ::= highFashionabilityFilter()
            } else {
                filters ::= fashionabilityFilter()
            }
        }
        if (look.hasColor) {
            filters ::= baseColorTopFilter()
        }

        filters
    }

    private def checkOut(look: Look): Look = {
        if (look.coating.get.isWeird) {
            look.hasWeirdElement = true
        }
        if (look.coating.get.color != "base") {
            look.hasColor = true
        }
        look
    }
}
