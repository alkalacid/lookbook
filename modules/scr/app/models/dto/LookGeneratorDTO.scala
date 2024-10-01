package models.dto

import models.dao.entities.{Bottom, Hairstyle, Jewelry, Makeup, Shoes, Top}
import play.api.libs.json.{Json, Reads, Writes}

case class LookGeneratorDTO(
                 var top: Option[Top] = None,
                 var bottom: Option[Bottom] = None,
                 var shoes: Option[Shoes] = None,
                 var coating: Option[Top] = None,
                 var hairstyle: Option[Hairstyle] = None,
                 var makeup: Option[Makeup] = None,
                 var jewelry: List[Jewelry] = List.empty,
                 var hasWeirdElement: Boolean = false,
                 var hasColor: Boolean = false,
                 var hasDress: Boolean = false,
                 var length: String = "mid"
          )

object LookGeneratorDTO {
    implicit val reads: Reads[LookGeneratorDTO] = Json.reads[LookGeneratorDTO]
    implicit val writes: Writes[LookGeneratorDTO] = Json.writes[LookGeneratorDTO]
}