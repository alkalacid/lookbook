package models.dao.entities

import play.api.libs.json.{Json, Reads, Writes}

import java.util.UUID

case class Top(
                override val id: String = UUID.randomUUID.toString.replaceAll("-", ""),
                override val name: String,
                override val image: String = "",
                isSleeve: Boolean = false,
                isDress: Boolean = false,
                isCoating: String = "noCoating",
                override val isWeird: Boolean = false,
                override val fashionability: Int = 0,
                isOpen: Boolean = false,
                override val color: String = "base",
                override val style: String = "base",
                length: String = "standard"
              ) extends ClothingItem

object Top {
    implicit val reads: Reads[Top] = Json.reads[Top]
    implicit val writes: Writes[Top] = Json.writes[Top]
}