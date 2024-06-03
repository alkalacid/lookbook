package models.dao.entities

import play.api.libs.json.{Json, Reads, Writes}

case class Top(
                override val id: String,
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
              ) extends LookElement

object Top {
    implicit val reads: Reads[Top] = Json.reads[Top]
    implicit val writes: Writes[Top] = Json.writes[Top]
}