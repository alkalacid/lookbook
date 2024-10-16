package models.dao.entities

import play.api.libs.json.{Json, Reads, Writes}

import java.util.UUID

case class Hairstyle(
                override val id: String = UUID.randomUUID.toString.replaceAll("-", ""),
                override val name: String = "",
                override val image: String = "",
                stylingDegree: Int = 0,
                override val isWeird: Boolean = false
              ) extends LookBookItem

object Hairstyle {
    implicit val reads: Reads[Hairstyle] = Json.reads[Hairstyle]
    implicit val writes: Writes[Hairstyle] = Json.writes[Hairstyle]
}