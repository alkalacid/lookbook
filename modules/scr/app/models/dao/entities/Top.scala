package models.dao.entities

import org.squeryl.KeyedEntity
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Json, Reads, Writes}

case class Top(
              id: String,
              name: String,
              image: String = "",
              isSleeve: Boolean = false,
              isDress: Boolean = false,
              isCoating: String = "noCoating",
              isWeird: Boolean = false,
              fashionability: Int = 0,
              isOpen: Boolean = false,
              color: String = "base",
              style: String = "base",
              length: String = "standard"
              ) extends KeyedEntity[String]

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