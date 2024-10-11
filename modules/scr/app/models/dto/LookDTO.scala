package models.dto

import models.dao.entities.Look
import play.api.libs.json.{Json, Reads, Writes}

case class LookDTO(
                  look: Look,
                  jewelry: Option[Set[String]] = None
          )

object LookDTO {
    implicit val reads: Reads[LookDTO] = Json.reads[LookDTO]
    implicit val writes: Writes[LookDTO] = Json.writes[LookDTO]
}