package models.dao.repositories

import models.dao.entities.{Jewelry, JewelryToLook, Look}
import models.dao.schema.LookBookSchema
import org.squeryl.Table
import org.squeryl.dsl.ast.LogicalBoolean

trait LookRepository extends CrudRepository[String, Look]

class LookRepositoryImpl extends LookRepository {
  override def defaultTable: Table[Look] = LookBookSchema.looks
}

trait JewelryToLookRepository extends CrudRepository[String, JewelryToLook]

class JewelryToLookRepositoryImpl extends JewelryToLookRepository {
  override def defaultTable: Table[JewelryToLook] = LookBookSchema.jewelryToLook
  private def jewelryTable: Table[Jewelry] = LookBookSchema.jewelry
  private def lookIdFilter(lookId: String): JewelryToLook => LogicalBoolean = _.look === lookId

  def getJewelryByLook(lookId: String): Set[Jewelry] = transaction{
    val jewelryIds: List[String] = filter(List(lookIdFilter(lookId))).map(jewelryToLook => jewelryToLook.jewelry)
    from(jewelryTable)(j => where(j.id in jewelryIds) select j).toSet
  }

  def deleteJewelryToLookByLook(lookId: String): Int = transaction {
    defaultTable.deleteWhere(j => j.look === lookId)
  }
}