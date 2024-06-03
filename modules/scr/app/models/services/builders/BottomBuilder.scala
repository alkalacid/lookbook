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
    private def noMidFilter(): Bottom => LogicalBoolean = _.length <> "mid"
    private def noMidiFilter(): Bottom => LogicalBoolean = _.length <> "midi"

    override def generate(look: Look, filterByWeather: String, filterByEvent: String): Look = {
        if (look.top.get.isDress) {
            look.length = look.top.get.length
            look
        } else {
            val filters = getFilters(look, filterByWeather, filterByEvent)

            if (filters.nonEmpty) {
                look.bottom = Some(Random.shuffle(bottomRepository.filter(filters = filters)).head)
            } else {
                look.bottom = Some(Random.shuffle(bottomRepository.list()).head)
            }
            look.length = look.bottom.get.length
            checkOut(look, look.bottom)
        }
    }

    override def getFilters(look: Look, filterByWeather: String, filterByEvent: String): List[Bottom => LogicalBoolean] = {
        var filters: List[Bottom => LogicalBoolean] = checkIn(look)

        if (look.coating.isDefined) {
            if (look.coating.get.length == "hip" || look.top.get.length == "hip") {
                filters ::= noMidFilter()
                filters ::= noMidiFilter()
            }
        }
        if (look.top.get.isOpen) {
            filters ::= noMiniFilter()
        }

        getFiltersByEvent(filterByEvent) ::: getFilterByRelaxEvent(filterByEvent) ::: filters
    }
}
