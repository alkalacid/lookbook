package models.dao.schema

import models.dao.entities.{Bottom, Shoes, Top}
import org.squeryl.{KeyedEntity, Schema, Table}

object LookBookSchema extends Schema{
    val tops: Table[Top] = table[Top]
    val bottoms: Table[Bottom] = table[Bottom]
    val shoes: Table[Shoes] = table[Shoes]

    def get(clothingType: String = "top"): Table[_ >: Top with Bottom with Shoes <: KeyedEntity[String]] = clothingType match {
        case "top" => tops
        case "bottom" => bottoms
        case "shoes" => shoes
    }
}
