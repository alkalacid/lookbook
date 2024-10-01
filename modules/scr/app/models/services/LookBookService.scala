package models.services

import com.google.inject.Inject
import models.dao.entities.{JewelryToLook, Look}
import models.dao.repositories._
import models.dto.{LookDTO, LookGeneratorDTO, LookWithItemsDTO}
import models.services.builders.decorator._
import models.services.builders._

trait LookBookService {
  def generateLook(weather: String = "any",
                   event: String = "any",
                   tailDay: String = "low"): LookGeneratorDTO

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

  def generateLook(weather: String = "any",
                   event: String = "any",
                   tailDay: String = "low"): LookGeneratorDTO = {

    @tailrec
    def recursiveGenerateLook(builders: List[AbstractBuilder], look: LookGeneratorDTO): LookGeneratorDTO= builders match {
          case head :: tail =>
            val newLook = head.generate(look)
            recursiveGenerateLook(tail, newLook)

          case Nil => look
    }

    recursiveGenerateLook(builders, new LookGeneratorDTO(weather = weather, event = event, tailDay = tailDay))
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
        top = topBuilder.topRepository.find(look.top.getOrElse("")),
        bottom = bottomBuilder.bottomRepository.find(look.bottom.getOrElse("")),
        shoes = shoesBuilder.shoesRepository.find(look.shoes.getOrElse("")),
        coating = topBuilder.topRepository.find(look.coating.getOrElse("")),
        hairstyle = hairstyleBuilder.hairstyleRepository.find(look.hairstyle.getOrElse("")),
        makeup = makeupBuilder.makeupRepository.find(look.makeup.getOrElse("")),
        comment = look.comment,
        jewelry = jewelryToLookRepository.getJewelryByLook(look.id)
      )
    }
  }
}
