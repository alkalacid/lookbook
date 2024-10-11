package models.dao.entities

import java.util.UUID

class ClothingItem extends LookBookItem {
  override def id: String = UUID.randomUUID.toString.replaceAll("-", "")
  override def name: String = ""
  override def image: String = ""
  override def isWeird: Boolean = false
  def fashionability: Int = 0
  def color: String = "base"
  def style: String = "base"
}