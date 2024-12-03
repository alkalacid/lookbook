package controllers

import com.google.inject.Inject
import models.dao.entities.Hairstyle._
import models.dao.entities.Jewelry._
import models.dao.entities.Makeup._
import models.dao.entities._
import models.dao.repositories._

class TopController @Inject()(override val repository: TopRepositoryImpl) extends LookItemController[Top, TopRepositoryImpl](repository) {
  override def addIdToItem(item: Top): Top = new Top(
    name = item.name,
    image = item.image,
    isSleeve = item.isSleeve,
    isDress = item.isDress,
    isCoating = item.isCoating,
    isWeird = item.isWeird,
    fashionability = item.fashionability,
    isOpen = item.isOpen,
    color = item.color,
    style = item.style,
    length = item.length
  )

  override def setImageName(item: Top, newImageName: String): Top = new Top(
      id = item.id,
      name = item.name,
      image = newImageName,
      isSleeve = item.isSleeve,
      isDress = item.isDress,
      isCoating = item.isCoating,
      isWeird = item.isWeird,
      fashionability = item.fashionability,
      isOpen = item.isOpen,
      color = item.color,
      style = item.style,
      length = item.length
    )
}

class BottomController @Inject()(override val repository: BottomRepositoryImpl) extends LookItemController[Bottom, BottomRepositoryImpl](repository) {
  override def addIdToItem(item: Bottom): Bottom = new Bottom(
    name = item.name,
    image = item.image,
    length = item.length,
    isWeird = item.isWeird,
    fashionability = item.fashionability,
    color = item.color,
    style = item.style
  )

  override def setImageName(item: Bottom, newImageName: String): Bottom = new Bottom(
    id = item.id,
    name = item.name,
    image = newImageName,
    length = item.length,
    isWeird = item.isWeird,
    fashionability = item.fashionability,
    color = item.color,
    style = item.style
  )
}

class ShoesController @Inject()(override val repository: ShoesRepositoryImpl) extends LookItemController[Shoes, ShoesRepositoryImpl](repository) {
  override def addIdToItem(item: Shoes): Shoes = new Shoes(
    name = item.name,
    image = item.image,
    isHeel = item.isHeel,
    isWarm = item.isWarm,
    isHigh = item.isHigh,
    isWeird = item.isWeird,
    isOpen = item.isOpen,
    fashionability = item.fashionability,
    color = item.color,
    style = item.style
  )

  override def setImageName(item: Shoes, newImageName: String): Shoes = new Shoes(
    id = item.id,
    name = item.name,
    image = newImageName,
    isHeel = item.isHeel,
    isWarm = item.isWarm,
    isHigh = item.isHigh,
    isWeird = item.isWeird,
    isOpen = item.isOpen,
    fashionability = item.fashionability,
    color = item.color,
    style = item.style
  )
}

class HairstyleController @Inject()(override val repository: HairstyleRepositoryImpl) extends LookItemController[Hairstyle, HairstyleRepositoryImpl](repository) {
  override def addIdToItem(item: Hairstyle): Hairstyle = new Hairstyle(
    name = item.name,
    image = item.image,
    stylingDegree = item.stylingDegree,
    isWeird = item.isWeird
  )

  override def setImageName(item: Hairstyle, newImageName: String): Hairstyle = new Hairstyle(
    id = item.id,
    name = item.name,
    image = newImageName,
    stylingDegree = item.stylingDegree,
    isWeird = item.isWeird
  )
}

class MakeupController @Inject()(override val repository: MakeupRepositoryImpl) extends LookItemController[Makeup, MakeupRepositoryImpl](repository) {
  override def addIdToItem(item: Makeup): Makeup = new Makeup(
    name = item.name,
    image = item.image,
    style = item.style,
    area = item.area,
    isWeird = item.isWeird
  )

  override def setImageName(item: Makeup, newImageName: String): Makeup = new Makeup(
    id = item.id,
    name = item.name,
    image = newImageName,
    style = item.style,
    area = item.area,
    isWeird = item.isWeird
  )
}

class JewelryController @Inject()(override val repository: JewelryRepositoryImpl) extends LookItemController[Jewelry, JewelryRepositoryImpl](repository) {
  override def addIdToItem(item: Jewelry): Jewelry = new Jewelry(
    name = item.name,
    image = item.image,
    area = item.area,
    isWeird = item.isWeird
  )

  override def setImageName(item: Jewelry, newImageName: String): Jewelry = new Jewelry(
    id = item.id,
    name = item.name,
    image = newImageName,
    area = item.area,
    isWeird = item.isWeird
  )
}
