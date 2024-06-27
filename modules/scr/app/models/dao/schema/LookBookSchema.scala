package models.dao.schema

import models.dao.entities._
import org.squeryl.{KeyedEntity, Schema, Table}

object LookBookSchema extends Schema{
    val tops: Table[Top] = table[Top]
    val bottoms: Table[Bottom] = table[Bottom]
    val shoes: Table[Shoes] = table[Shoes]
    val hairstyle: Table[Hairstyle] = table[Hairstyle]
    val makeup: Table[Makeup] = table[Makeup]
    val jewelry: Table[Jewelry] = table[Jewelry]
    val looks: Table[Look] = table[Look]

    def get(clothingType: String = "top"): Table[_ <: KeyedEntity[String]] = clothingType match {
        case "top" => tops
        case "bottom" => bottoms
        case "shoes" => shoes
        case "hairstyle" => hairstyle
        case "makeup" => makeup
        case "jewelry" => jewelry
        case "look" => looks
    }
}
