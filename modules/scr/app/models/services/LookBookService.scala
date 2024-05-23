package models.services

import com.google.inject.Inject
import models.dto.Look
import models.services.builders.{BottomBuilder, CoatingBuilder, ShoesBuilder, TopBuilder}

trait LookBookService {
  def generateLook(filterByWeather: String, filterByEvent: String): Look
}

class LookBookServiceImpl @Inject()(
                                        val topBuilder: TopBuilder,
                                        val bottomBuilder: BottomBuilder,
                                        val shoesBuilder: ShoesBuilder,
                                        val coatingBuilder: CoatingBuilder
                                   ) extends LookBookService {

  def generateLook(filterByWeather: String, filterByEvent: String): Look = {
        var look = new Look
        look = topBuilder.generate(look, filterByWeather, filterByEvent)
        look = coatingBuilder.generate(look, filterByWeather, filterByEvent)
        look = bottomBuilder.generate(look, filterByWeather, filterByEvent)
        shoesBuilder.generate(look, filterByWeather, filterByEvent)
      }
}
