package models.dao.entities

import org.squeryl.KeyedEntity
import play.api.libs.json.{Json, Reads, Writes}

import java.util.UUID

case class Look(
                 id: String = UUID.randomUUID.toString.replaceAll("-", ""),
                 top: Option[String] = None,
                 bottom: Option[String] = None,
                 shoes: Option[String] = None,
                 coating: Option[String] = None,
                 hairstyle: Option[String] = None,
                 makeup: Option[String] = None,
                 weather: Option[String] = None,
                 event: Option[String] = None,
                 comment: Option[String] = None
          ) extends KeyedEntity[String]

object Look {
    implicit val reads: Reads[Look] = Json.reads[Look]
    implicit val writes: Writes[Look] = Json.writes[Look]
}