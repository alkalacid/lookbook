package controllers

import com.google.inject.Inject
import models.dao.entities.Hairstyle._
import models.dao.entities.Jewelry._
import models.dao.entities.Makeup._
import models.dao.entities._
import models.dao.repositories._

class TopController @Inject()(override val repository: TopRepositoryImpl) extends LookItemController[Top, TopRepositoryImpl](repository)
class BottomController @Inject()(override val repository: BottomRepositoryImpl) extends LookItemController[Bottom, BottomRepositoryImpl](repository)
class ShoesController @Inject()(override val repository: ShoesRepositoryImpl) extends LookItemController[Shoes, ShoesRepositoryImpl](repository)
class HairstyleController @Inject()(override val repository: HairstyleRepositoryImpl) extends LookItemController[Hairstyle, HairstyleRepositoryImpl](repository)
class MakeupController @Inject()(override val repository: MakeupRepositoryImpl) extends LookItemController[Makeup, MakeupRepositoryImpl](repository)
class JewelryController @Inject()(override val repository: JewelryRepositoryImpl) extends LookItemController[Jewelry, JewelryRepositoryImpl](repository)
