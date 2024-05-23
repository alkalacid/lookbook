package models.services.builders

import models.dto.Look
import org.squeryl.KeyedEntity
import org.squeryl.dsl.ast.LogicalBoolean

trait Builder[K, Entity <: KeyedEntity[K]] {

    def generate(look: Look, filterByWeather: String, filterByEvent: String): Look
    def getFilters(look: Look, filterByWeather: String, filterByEvent: String): List[Entity => LogicalBoolean]
}
