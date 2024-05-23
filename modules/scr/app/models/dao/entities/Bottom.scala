package models.dao.entities

import org.squeryl.KeyedEntity
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Json, Reads, Writes}

case class Bottom(
              id: String,
              name: String,
              image: String = "",
              length: String = "mini",
              isWeird: Boolean = false,
              fashionability: Int = 0,
              color: String = "base",
              style: String = "base"
              ) extends KeyedEntity[String]

object Bottom {
    implicit val reads: Reads[Bottom] = Json.reads[Bottom]

    implicit val writes: Writes[Bottom] = (
        (JsPath \ "id").write[String] and
            (JsPath \ "name").write[String] and
            (JsPath \ "image").write[String] and
            (JsPath \ "length").write[String] and
            (JsPath \ "isWeird").write[Boolean] and
            (JsPath \ "fashionability").write[Int] and
            (JsPath \ "color").write[String] and
            (JsPath \ "style").write[String]
        )(unlift(Bottom.unapply))
}