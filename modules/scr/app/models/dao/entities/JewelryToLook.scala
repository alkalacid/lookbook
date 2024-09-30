package models.dao.entities

import org.squeryl.KeyedEntity
import play.api.libs.json.{Json, Reads, Writes}

import java.util.UUID

case class JewelryToLook(
                 id: String = UUID.randomUUID.toString.replaceAll("-", ""),
                 jewelry: String,
                 look: String
          ) extends KeyedEntity[String]

object JewelryToLook {
    implicit val reads: Reads[JewelryToLook] = Json.reads[JewelryToLook]
    implicit val writes: Writes[JewelryToLook] = Json.writes[JewelryToLook]
}