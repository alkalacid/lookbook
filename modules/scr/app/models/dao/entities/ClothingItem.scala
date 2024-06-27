package models.dao.entities

class ClothingItem extends LookBookItem {
  override def id: String = ""
  override def name: String = ""
  override def image: String = ""
  override def isWeird: Boolean = false
  def fashionability: Int = 0
  def color: String = "base"
  def style: String = "base"
}