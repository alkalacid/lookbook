package models.dto

import models.dao.entities._
import play.api.libs.json.{Json, Reads, Writes}

case class LookDTO(
                    top: Option[String] = None,
                    bottom: Option[String] = None,
                    shoes: Option[String] = None,
                    coating: Option[String] = None,
                    hairstyle: Option[String] = None,
                    makeup: Option[String] = None,
                    weather: Option[String] = None,
                    event: Option[String] = None,
                    comment: Option[String] = None,
                    jewelry: Option[Set[Jewelry]] = None
          )

object LookDTO {
    implicit val reads: Reads[LookDTO] = Json.reads[LookDTO]
    implicit val writes: Writes[LookDTO] = Json.writes[LookDTO]
}