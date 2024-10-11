package models.dao.entities

import org.squeryl.KeyedEntity

import java.util.UUID
import scala.language.implicitConversions

class LookBookItem extends KeyedEntity[String] {
  override def id: String = UUID.randomUUID.toString.replaceAll("-", "")
  def name: String = ""
  def image: String = ""
  def isWeird: Boolean = false
}