package models.dao.entities

import org.squeryl.KeyedEntity
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Json, Reads, Writes}

case class Shoes(
              id: String,
              name: String,
              image: String = "",
              isHeel: Boolean = false,
              isWarm: Boolean = false,
              isHigh: Boolean = false,
              isWeird: Boolean = false,
              fashionability: Int = 0,
              isOpen: Boolean = false,
              color: String = "base",
              style: String = "base"
              ) extends KeyedEntity[String]

object Shoes {
    implicit val reads: Reads[Shoes] = Json.reads[Shoes]

    implicit val writes: Writes[Shoes] = (
        (JsPath \ "id").write[String] and
            (JsPath \ "name").write[String] and
            (JsPath \ "image").write[String] and
            (JsPath \ "isHeel").write[Boolean] and
            (JsPath \ "isWarm").write[Boolean] and
            (JsPath \ "isHigh").write[Boolean] and
            (JsPath \ "isWeird").write[Boolean] and
            (JsPath \ "fashionability").write[Int] and
            (JsPath \ "isOpen").write[Boolean] and
            (JsPath \ "color").write[String] and
            (JsPath \ "style").write[String]
        )(unlift(Shoes.unapply))
}