package models.services.builders

import models.dao.entities.LookElement
import models.dto.Look
import org.squeryl.KeyedEntity
import org.squeryl.dsl.ast.LogicalBoolean
import org.squeryl.PrimitiveTypeMode._
import scala.util.Random

trait Builder[K, Entity <: KeyedEntity[K]] {
    protected def noWeirdFilter(): LookElement => LogicalBoolean = _.isWeird === false
    protected def baseColorFilter(): LookElement => LogicalBoolean = _.color === "base"
    protected def sportStyleFilter(): LookElement => LogicalBoolean = _.style === "sport"
    protected def highFashionabilityFilter(): LookElement => LogicalBoolean = _.fashionability gte 50
    protected def fashionabilityFilter(): LookElement => LogicalBoolean = _.fashionability gt 0

    def generate(look: Look, filterByWeather: String, filterByEvent: String): Look
    protected def getFilters(look: Look, filterByWeather: String, filterByEvent: String): List[Entity => LogicalBoolean]

    protected def checkIn(look: Look): List[LookElement => LogicalBoolean] = {
        var filters: List[LookElement => LogicalBoolean] = List.empty
        if (look.hasWeirdElement) filters ::= noWeirdFilter()
        if (look.hasColor) filters ::= baseColorFilter()
        filters
    }

    protected def checkOut(look: Look, lookPiece: Option[LookElement]): Look = lookPiece match {
            case Some(x) =>
                if (x.isWeird) look.hasWeirdElement = true
                if (x.color != "base") look.hasColor = true
                look
            case _ => look
    }

    protected def getFiltersByEvent(event: String): List[LookElement => LogicalBoolean] = {

        var filters: List[LookElement => LogicalBoolean] = List.empty

        if ((event == "celebrate") || event == "fashion") {
            filters ::= noWeirdFilter()
        }

        if (event == "fashion") {
            val rand: Int = Random.nextInt(2)
            if (rand == 2) {
                filters ::= highFashionabilityFilter()
            } else {
                filters ::= fashionabilityFilter()
            }
        }

        filters
    }

    protected def getFilterByRelaxEvent(event: String): List[LookElement => LogicalBoolean] = {

        var filters: List[LookElement => LogicalBoolean] = List.empty

        if (event == "relax") {
            val rand: Int = Random.nextInt(2)
            if (rand == 2) {
                filters ::= sportStyleFilter()
            }
        }

        filters
    }
}
