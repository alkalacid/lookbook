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
        checkOut(look, look.top)
    }

    override def getFilters(look: Look, filterByWeather: String, filterByEvent: String): List[Top => LogicalBoolean] = {
        var filters: List[Top => LogicalBoolean] = List.empty
        filters ::= noCoatingFilter()

        if (filterByWeather == "heat") {
            filters ::= noSleevesFilter()
        }

        getFiltersByEvent(filterByEvent) ::: getFilterByRelaxEvent(filterByEvent) ::: filters
    }
}
