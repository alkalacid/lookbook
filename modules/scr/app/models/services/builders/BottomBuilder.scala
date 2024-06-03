package models.services.builders

import com.google.inject.Inject
import models.dao.entities.Bottom
import models.dao.repositories.BottomRepositoryImpl
import models.dto.Look
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.dsl.ast.LogicalBoolean

trait BottomBuilder extends Builder[String, Bottom, BottomRepositoryImpl]

class BottomBuilderImpl @Inject()(val bottomRepository: BottomRepositoryImpl) extends BottomBuilder {

    private def noMidFilter(): Bottom => LogicalBoolean = _.length <> "mid"
    private def noMidiFilter(): Bottom => LogicalBoolean = _.length <> "midi"
    private def noMaxFilter(): Bottom => LogicalBoolean = _.length <> "max"

    override def generate(look: Look, filterByWeather: String, filterByEvent: String): Look = {
        if (look.top.get.isDress) {
            look.length = look.top.get.length
            look
        } else {
            val filters: List[Bottom => LogicalBoolean] = getFilters(look, filterByWeather, filterByEvent)
            val bottom: Option[Bottom] = getElementFromDatabase(filters)(bottomRepository)

            if(bottom.isEmpty) {
                throw new Exception("No bottom was found")
            } else {
                look.bottom = bottom
            }

            look.length = look.bottom.get.length
            checkOut(look, look.bottom)
        }
    }

    override def getFilters(look: Look, filterByWeather: String, filterByEvent: String): List[Bottom => LogicalBoolean] = {
        var filters: List[Bottom => LogicalBoolean] = checkIn(look)

        if (filterByWeather == "heat") {
            filters ::= noMaxFilter()
        }

        getFiltersByLength(look) ::: getFiltersByEvent(filterByEvent) ::: getFilterByRelaxEvent(filterByEvent) ::: filters
    }

    private def getFiltersByLength(look: Look): List[Bottom => LogicalBoolean] = {
        if (look.top.get.length == "hip" || (
          look.coating.isDefined && look.coating.get.length == "hip"
          )) {
            List(noMidFilter(), noMidiFilter())
        } else List.empty
    }
}
