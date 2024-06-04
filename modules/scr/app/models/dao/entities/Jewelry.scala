package models.dao.entities

import play.api.libs.json.{Json, Reads, Writes}

case class Jewelry(
                override val id: String,
                override val name: String,
                override val image: String = "",
                override val isWeird: Boolean = false,
                area: String = "neck"
              ) extends DecoratorItem

object Jewelry {
    implicit val reads: Reads[Jewelry] = Json.reads[Jewelry]
    implicit val writes: Writes[Jewelry] = Json.writes[Jewelry]
}