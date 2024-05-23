package models.services.builders

import com.google.inject.Inject
import models.dao.entities.Bottom
import models.dao.repositories.BottomRepositoryImpl
import models.dto.Look
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.dsl.ast.LogicalBoolean
import scala.util.Random

trait BottomBuilder extends Builder[String, Bottom]

class BottomBuilderImpl @Inject()(val bottomRepository: BottomRepositoryImpl) extends BottomBuilder {

    private def noMiniFilter(): Bottom => LogicalBoolean = _.length <> "mini"
    private def sportStyleFilter(): Bottom => LogicalBoolean = _.style === "sport"
    private def highFashionabilityFilter(): Bottom => LogicalBoolean = _.fashionability gte 50
    private def fashionabilityFilter(): Bottom => LogicalBoolean = _.fashionability gt 0

    private def noWeirdBottomFilter(): Bottom => LogicalBoolean = _.isWeird === false
    private def baseColorBottomFilter(): Bottom => LogicalBoolean = _.color === "base"

    override def generate(look: Look, filterByWeather: String, filterByEvent: String): Look = {
        if (look.top.get.isDress) {
            look
        } else {
            val filters = getFilters(look, filterByWeather, filterByEvent)

            if (filters.nonEmpty) {
                look.bottom = Some(Random.shuffle(bottomRepository.filter(filters = filters)).head)
            } else {
                look.bottom =Some(Random.shuffle(bottomRepository.list()).head)
            }
            checkOut(look)
        }
    }

    override def getFilters(look: Look, filterByWeather: String, filterByEvent: String): List[Bottom => LogicalBoolean] = {
        var filters: List[Bottom => LogicalBoolean] = List.empty
        if (look.hasWeirdElement || filterByEvent == "celebrate" || filterByEvent == "fashion") {
            filters ::= noWeirdBottomFilter()
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
        if (look.hasColor) {
            filters ::= baseColorBottomFilter()
        }
        if (look.top.get.isOpen) {
            filters ::= noMiniFilter()
        }

        filters
    }

    private def checkOut(look: Look): Look = {
        if (look.bottom.get.isWeird) {
            look.hasWeirdElement = true
        }
        if (look.bottom.get.color != "base") {
            look.hasColor = true
        }
        look
    }
}
