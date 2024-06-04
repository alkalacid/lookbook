package models.dao.entities

import org.squeryl.KeyedEntity

class ClothingItem extends KeyedEntity[String] {
  override def id: String = ""
  def name: String = ""
  def image: String = ""
  def isWeird: Boolean = false
  def fashionability: Int = 0
  def color: String = "base"
  def style: String = "base"
}