package models.dto

import models.dao.entities._
import play.api.libs.json.{Json, Reads, Writes}

case class LookWithItemsDTO(
                    top: Option[Top] = None,
                    bottom: Option[Bottom] = None,
                    shoes: Option[Shoes] = None,
                    coating: Option[Top] = None,
                    hairstyle: Option[Hairstyle] = None,
                    makeup: Option[Makeup] = None,
                    comment: Option[String] = None,
                    jewelry: Set[Jewelry] = Set.empty
          )

object LookWithItemsDTO {
    implicit val reads: Reads[LookWithItemsDTO] = Json.reads[LookWithItemsDTO]
    implicit val writes: Writes[LookWithItemsDTO] = Json.writes[LookWithItemsDTO]
}