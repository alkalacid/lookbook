package models.dao.entities

import play.api.libs.json.{Json, Reads, Writes}

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
              ) extends ClothingItem

object Shoes {
    implicit val reads: Reads[Shoes] = Json.reads[Shoes]
    implicit val writes: Writes[Shoes] = Json.writes[Shoes]

}