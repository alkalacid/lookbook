package models.dao.repositories

import models.dao.entities.{Bottom, Shoes, Top}
import models.dao.schema.LookBookSchema
import org.squeryl.Table

trait TopRepository extends CrudRepository[String, Top]

class TopRepositoryImpl extends TopRepository {
  override def defaultTable: Table[Top] = LookBookSchema.tops
}


trait BottomRepository extends CrudRepository[String, Bottom]

class BottomRepositoryImpl extends BottomRepository {
  override def defaultTable: Table[Bottom] = LookBookSchema.bottoms
}


trait ShoesRepository extends CrudRepository[String, Shoes]

class ShoesRepositoryImpl extends ShoesRepository {
  override def defaultTable: Table[Shoes] = LookBookSchema.shoes
}