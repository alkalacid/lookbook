package models.dao.entities

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Json, Reads, Writes}

case class Bottom(
                   override val id: String,
                   override val name: String,
                   override val image: String = "",
                   length: String = "mini",
                   override val isWeird: Boolean = false,
                   override val fashionability: Int = 0,
                   override val color: String = "base",
                   override val style: String = "base"
              ) extends LookElement

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