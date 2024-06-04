package models.dao.entities

import org.squeryl.KeyedEntity

class DecoratorItem extends KeyedEntity[String] {
  override def id: String = ""
  def name: String = ""
  def image: String = ""
  def isWeird: Boolean = false
}