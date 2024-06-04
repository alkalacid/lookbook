package models.services

import com.google.inject.Inject
import models.dto.Look
import models.services.builders.decorator.{HairstyleBuilder, JewelryBuilder, MakeupBuilder}
import models.services.builders.{BottomBuilder, CoatingBuilder, ShoesBuilder, TopBuilder}

trait LookBookService {
  def generateLook(filters: Map[String, Seq[String]]): Look
}

class LookBookServiceImpl @Inject()(
                                        val topBuilder: TopBuilder,
                                        val bottomBuilder: BottomBuilder,
                                        val shoesBuilder: ShoesBuilder,
                                        val coatingBuilder: CoatingBuilder,
                                        val hairstyleBuilder: HairstyleBuilder,
                                        val makeupBuilder: MakeupBuilder,
                                        val jewelryBuilder: JewelryBuilder
                                   ) extends LookBookService {

  def generateLook(filters: Map[String, Seq[String]]): Look = {
        var look = new Look
        val filterByWeather = filters("weather").head
        val filterByEvent = filters("event").head

        look = topBuilder.generate(look, filterByWeather, filterByEvent)
        look = coatingBuilder.generate(look, filterByWeather, filterByEvent)
        look = bottomBuilder.generate(look, filterByWeather, filterByEvent)
        look = shoesBuilder.generate(look, filterByWeather, filterByEvent)
        look = hairstyleBuilder.generate(look, filters)
        look = makeupBuilder.generate(look, filters)
        jewelryBuilder.generate(look, filters)
      }
}
