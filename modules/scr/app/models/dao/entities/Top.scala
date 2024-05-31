package models.dao.entities

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Json, Reads, Writes}

case class Top(
                override val id: String,
                override val name: String,
                override val image: String = "",
                isSleeve: Boolean = false,
                isDress: Boolean = false,
                isCoating: String = "noCoating",
                override val isWeird: Boolean = false,
                override val fashionability: Int = 0,
                isOpen: Boolean = false,
                override val color: String = "base",
                override val style: String = "base",
                length: String = "standard"
              ) extends LookElement

object Top {
    implicit val reads: Reads[Top] = Json.reads[Top]

      implicit val writes: Writes[Top] = (
          (JsPath \ "id").write[String] and
              (JsPath \ "name").write[String] and
              (JsPath \ "image").write[String] and
              (JsPath \ "isSleeve").write[Boolean] and
              (JsPath \ "isDress").write[Boolean] and
              (JsPath \ "isCoating").write[String] and
              (JsPath \ "isWeird").write[Boolean] and
              (JsPath \ "fashionability").write[Int] and
              (JsPath \ "isOpen").write[Boolean] and
              (JsPath \ "color").write[String] and
              (JsPath \ "style").write[String] and
              (JsPath \ "length").write[String]
          )(unlift(Top.unapply))
}