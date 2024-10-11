package models.services

import com.google.inject.Inject
import models.dao.entities.{JewelryToLook, Look}
import models.dao.repositories._
import models.dto.{LookDTO, LookFilterDTO, LookGeneratorDTO, LookWithItemsDTO}
import models.services.builders.decorator._
import models.services.builders._
import org.squeryl.PrimitiveTypeMode.transaction

import scala.annotation.tailrec

trait LookBookService {
  def generateLook(filters: LookFilterDTO): LookGeneratorDTO
  def add(look: LookDTO): LookWithItemsDTO
  def update(look: LookDTO): LookWithItemsDTO
  def delete(look: LookDTO): Unit
  def get: Seq[LookWithItemsDTO]
}

class LookBookServiceImpl @Inject()(
                                     val topBuilder: TopClothingBuilderImpl,
                                     val bottomBuilder: BottomClothingBuilderImpl,
                                     val shoesBuilder: ShoesClothingBuilderImpl,
                                     val coatingBuilder: CoatingClothingBuilderImpl,
                                     val hairstyleBuilder: HairstyleBuilderImpl,
                                     val makeupBuilder: MakeupBuilderImpl,
                                     val jewelryBuilder: JewelryBuilderImpl,
                                     val lookRepository: LookRepositoryImpl,
                                     val jewelryToLookRepository: JewelryToLookRepositoryImpl
                                   ) extends LookBookService {
  val builders: List[AbstractBuilder] = List(
    topBuilder,
    coatingBuilder,
    bottomBuilder,
    shoesBuilder,
    hairstyleBuilder,
    makeupBuilder,
    jewelryBuilder
  )

  def generateLook(filters: LookFilterDTO): LookGeneratorDTO = {
    val lookDTO: LookGeneratorDTO = new LookGeneratorDTO(weather = filters.weather, event = filters.event, tailDay = filters.tailDay)
    if (filters.predefinedItemId.isDefined) {
      val itemType: String = filters.predefinedItemType.getOrElse(throw new Exception("No itemType for itemId"))
      val firstBuilder: AbstractBuilder = builders.filter(_.builderName == itemType).head
      val otherBuilders: List[AbstractBuilder] = builders.filter(_.builderName != itemType)
      val look: LookGeneratorDTO = firstBuilder.generate(lookDTO, filters.predefinedItemId.get)
      recursiveGenerateLook(otherBuilders, look)
    } else {
      recursiveGenerateLook(builders, lookDTO)

    }
  }

  override def add(look: LookDTO): LookWithItemsDTO = transaction {

    val newLook = lookRepository.insert(toLook(look))
    linkJewelryToLook(newLook.id, look.jewelry)

    toLookWithItemsDTO(newLook)
  }

  override def update(look: LookDTO): LookWithItemsDTO = transaction {

    lookRepository.update(look.look)
    jewelryToLookRepository.deleteJewelryToLookByLook(look.look.id)
    linkJewelryToLook(look.look.id, look.jewelry)

    toLookWithItemsDTO(look.look)
  }

  override def delete(look: LookDTO): Unit = {

    jewelryToLookRepository.deleteJewelryToLookByLook(look.look.id)
    lookRepository.delete(look.look)
  }

  override def get: Seq[LookWithItemsDTO] = {
    lookRepository.list().map(toLookWithItemsDTO)
  }

  @tailrec
  private def recursiveGenerateLook(builders: List[AbstractBuilder], look: LookGeneratorDTO): LookGeneratorDTO= builders match {
      case head :: tail =>
        val newLook = head.generate(look)
        recursiveGenerateLook(tail, newLook)

      case Nil => look
  }

  private def linkJewelryToLook(lookId: String, jewelrySet: Option[Set[String]]): Unit = {
    jewelrySet.map( jewelry =>
      jewelry.map (j =>
        jewelryToLookRepository.insert(new JewelryToLook(
          jewelry = j,
          look = lookId
        ))
      )
    )
  }

  private def toLookWithItemsDTO(look: Look): LookWithItemsDTO = {
    new LookWithItemsDTO(
      top = topBuilder.repository.find(look.top.getOrElse("")),
      bottom = bottomBuilder.repository.find(look.bottom.getOrElse("")),
      shoes = shoesBuilder.repository.find(look.shoes.getOrElse("")),
      coating = topBuilder.repository.find(look.coating.getOrElse("")),
      hairstyle = hairstyleBuilder.repository.find(look.hairstyle.getOrElse("")),
      makeup = makeupBuilder.repository.find(look.makeup.getOrElse("")),
      comment = look.comment,
      jewelry = jewelryToLookRepository.getJewelryByLook(look.id)
    )
  }

  private def toLook(look: LookDTO): Look = {
    new Look(
      top = look.look.top,
      bottom = look.look.bottom,
      shoes = look.look.shoes,
      coating = look.look.coating,
      hairstyle = look.look.hairstyle,
      makeup = look.look.makeup,
      weather = look.look.weather,
      event = look.look.event,
      comment = look.look.comment
    )
  }
}
