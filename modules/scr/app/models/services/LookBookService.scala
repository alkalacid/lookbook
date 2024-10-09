package models.services

import com.google.inject.Inject
import models.dao.entities.{JewelryToLook, Look}
import models.dao.repositories._
import models.dto.{LookDTO, LookFilterDTO, LookGeneratorDTO, LookWithItemsDTO}
import models.services.builders.decorator._
import models.services.builders._

import scala.annotation.tailrec

trait LookBookService {
  def generateLook(filters: LookFilterDTO): LookGeneratorDTO

  def add(look: LookDTO): Look

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

  override def add(look: LookDTO): Look = {

    val newLook = lookRepository.insert(new Look(
      top = look.top,
      bottom = look.bottom,
      shoes = look.shoes,
      coating = look.coating,
      hairstyle = look.hairstyle,
      makeup = look.makeup,
      weather = look.weather,
      event = look.event,
      comment = look.comment
    ))

    if (look.jewelry.isDefined) {
      look.jewelry.get.foreach( j =>
        jewelryToLookRepository.insert(new JewelryToLook(
          jewelry = j.id,
          look = newLook.id
        ))
      )
    }

    newLook
  }

  override def get: Seq[LookWithItemsDTO] = {
    lookRepository.list().map { look =>
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
  }

  @tailrec
  private def recursiveGenerateLook(builders: List[AbstractBuilder], look: LookGeneratorDTO): LookGeneratorDTO= builders match {
      case head :: tail =>
        val newLook = head.generate(look)
        recursiveGenerateLook(tail, newLook)

      case Nil => look
  }
}
