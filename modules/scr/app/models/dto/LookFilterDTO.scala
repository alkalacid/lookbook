package models.dto

import play.api.libs.json.{Json, Reads, Writes}

case class LookFilterDTO(
                          weather: String = "any",
                          event: String = "any",
                          tailDay: String = "low",
                          predefinedItemId: Option[String] = None,
                          predefinedItemType: Option[String] = None
          )

object LookFilterDTO {
    implicit val reads: Reads[LookFilterDTO] = Json.reads[LookFilterDTO]
    implicit val writes: Writes[LookFilterDTO] = Json.writes[LookFilterDTO]
}