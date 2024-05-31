package models.services.builders

import models.dao.entities.LookElement
import models.dto.Look
import org.squeryl.KeyedEntity
import org.squeryl.dsl.ast.LogicalBoolean
import org.squeryl.PrimitiveTypeMode._

trait Builder[K, Entity <: KeyedEntity[K]] {
    def noWeirdFilter(): LookElement => LogicalBoolean = _.isWeird === false
    def baseColorFilter(): LookElement => LogicalBoolean = _.color === "base"

    def sportStyleFilter(): LookElement => LogicalBoolean = _.style === "sport"

    def highFashionabilityFilter(): LookElement => LogicalBoolean = _.fashionability gte 50
    def fashionabilityFilter(): LookElement => LogicalBoolean = _.fashionability gt 0

    def generate(look: Look, filterByWeather: String, filterByEvent: String): Look
    def getFilters(look: Look, filterByWeather: String, filterByEvent: String): List[Entity => LogicalBoolean]
}
