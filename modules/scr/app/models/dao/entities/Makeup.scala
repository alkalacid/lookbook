package models.dao.entities

import play.api.libs.json.{Json, Reads, Writes}

import java.util.UUID

case class Makeup(
                override val id: String = UUID.randomUUID.toString.replaceAll("-", ""),
                override val name: String,
                override val image: String = "",
                style: String = "base",
                area: String = "eyes",
                override val isWeird: Boolean = false
              ) extends LookBookItem

object Makeup {
    implicit val reads: Reads[Makeup] = Json.reads[Makeup]
    implicit val writes: Writes[Makeup] = Json.writes[Makeup]
}