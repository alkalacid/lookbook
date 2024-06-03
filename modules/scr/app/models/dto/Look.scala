package models.dto

import models.dao.entities.{Bottom, Shoes, Top}
import play.api.libs.json.{Json, Reads, Writes}

case class Look(
              var top: Option[Top] = None,
              var bottom: Option[Bottom] = None,
              var shoes: Option[Shoes] = None,
              var coating: Option[Top] = None,
              var hasWeirdElement: Boolean = false,
              var hasColor: Boolean = false,
              var length: String = "mid"
          )

object Look {
    implicit val reads: Reads[Look] = Json.reads[Look]
    implicit val writes: Writes[Look] = Json.writes[Look]
}