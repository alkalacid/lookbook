package models.dao.repositories

import models.dao.entities.{Bottom, Hairstyle, Jewelry, Makeup, Shoes, Top}
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

trait HairstyleRepository extends CrudRepository[String, Hairstyle]

class HairstyleRepositoryImpl extends HairstyleRepository {
  override def defaultTable: Table[Hairstyle] = LookBookSchema.hairstyle
}

trait MakeupRepository extends CrudRepository[String, Makeup]

class MakeupRepositoryImpl extends MakeupRepository {
  override def defaultTable: Table[Makeup] = LookBookSchema.makeup
}

trait JewelryRepository extends CrudRepository[String, Jewelry]

class JewelryRepositoryImpl extends JewelryRepository {
  override def defaultTable: Table[Jewelry] = LookBookSchema.jewelry
}