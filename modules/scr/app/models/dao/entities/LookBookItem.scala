package models.dao.entities

import org.squeryl.KeyedEntity

import scala.language.implicitConversions

class LookBookItem extends KeyedEntity[String] {
  override def id: String = ""
  def name: String = ""
  def image: String = ""
  def isWeird: Boolean = false
}