package models.services

import com.google.inject.Inject
import models.dto.LookGeneratorDTO
import models.services.builders.decorator.{HairstyleBuilder, JewelryBuilder, MakeupBuilder}
import models.services.builders.{BottomClothingBuilder, CoatingClothingBuilder, ShoesClothingBuilder, TopClothingBuilder}

trait LookBookService {
  def generateLook(filters: Map[String, Seq[String]]): LookGeneratorDTO
}

class LookBookServiceImpl @Inject()(
                                     val topBuilder: TopClothingBuilder,
                                     val bottomBuilder: BottomClothingBuilder,
                                     val shoesBuilder: ShoesClothingBuilder,
                                     val coatingBuilder: CoatingClothingBuilder,
                                     val hairstyleBuilder: HairstyleBuilder,
                                     val makeupBuilder: MakeupBuilder,
                                     val jewelryBuilder: JewelryBuilder
                                   ) extends LookBookService {

  def generateLook(filters: Map[String, Seq[String]]): LookGeneratorDTO = {
    jewelryBuilder.generate(
      makeupBuilder.generate(
        hairstyleBuilder.generate(
          shoesBuilder.generate(
            bottomBuilder.generate(
              coatingBuilder.generate(
                topBuilder.generate(new LookGeneratorDTO, filters),
                filters),
              filters),
            filters),
          filters),
        filters), 
      filters)
  }
}
