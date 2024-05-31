package models.dao.entities

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Json, Reads, Writes}

case class Shoes(
                  override val id: String,
                  override val name: String,
                  override val image: String = "",
                  isHeel: Boolean = false,
                  isWarm: Boolean = false,
                  isHigh: Boolean = false,
                  override val isWeird: Boolean = false,
                  override val fashionability: Int = 0,
                  isOpen: Boolean = false,
                  override val color: String = "base",
                  override val style: String = "base"
              ) extends LookElement

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