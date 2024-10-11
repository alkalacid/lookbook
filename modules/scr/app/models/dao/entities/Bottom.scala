package models.dao.entities

import play.api.libs.json.{Json, Reads, Writes}

import java.util.UUID

case class Bottom(
                   override val id: String = UUID.randomUUID.toString.replaceAll("-", ""),
                   override val name: String,
                   override val image: String = "",
                   length: String = "mini",
                   override val isWeird: Boolean = false,
                   override val fashionability: Int = 0,
                   override val color: String = "base",
                   override val style: String = "base"
              ) extends ClothingItem

object Bottom {
    implicit val reads: Reads[Bottom] = Json.reads[Bottom]
    implicit val writes: Writes[Bottom] = Json.writes[Bottom]
}