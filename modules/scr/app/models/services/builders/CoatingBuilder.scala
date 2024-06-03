package models.services.builders

import com.google.inject.Inject
import models.dao.entities.Top
import models.dao.repositories.TopRepositoryImpl
import models.dto.Look
import org.squeryl.dsl.ast.LogicalBoolean
import org.squeryl.PrimitiveTypeMode._

trait CoatingBuilder extends Builder[String, Top, TopRepositoryImpl]

class CoatingBuilderImpl @Inject()(val topRepository: TopRepositoryImpl) extends CoatingBuilder {

    private def coatingFilter(): Top => LogicalBoolean = _. isCoating <> "noCoating"

    override def generate(look: Look, filterByWeather: String, filterByEvent: String): Look = {
        if (look.top.get.isSleeve || filterByWeather == "heat") {
            look
        } else {
            val filters = getFilters(look, filterByWeather, filterByEvent)
            val coating: Option[Top] = getElementFromDatabase(filters)(topRepository)

            if(coating.isEmpty) {
                throw new Exception("No coating was found")
            } else {
                look.coating = coating
            }

            checkOut(look, look.coating)
        }
    }

    override def getFilters(look: Look, filterByWeather: String, filterByEvent: String): List[Top => LogicalBoolean] = {
        var filters: List[Top => LogicalBoolean] = checkIn(look)
        filters ::= coatingFilter()
        filters ::: getFiltersByEvent(filterByEvent)
    }
}
