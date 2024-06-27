package models.dao.repositories

import models.dao.entities.Look
import models.dao.schema.LookBookSchema
import org.squeryl.Table

trait LookRepository extends CrudRepository[String, Look]

class LookRepositoryImpl extends LookRepository {
  override def defaultTable: Table[Look] = LookBookSchema.looks
}