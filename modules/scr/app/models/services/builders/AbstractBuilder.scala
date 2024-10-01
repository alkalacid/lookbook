package models.services.builders

import models.dto.LookGeneratorDTO

trait AbstractBuilder {

  val builderName: String

  protected val weatherHeat: String = "heat"
  protected val weatherWinter = "winter"
  protected val weatherRoom = "room"
  protected val weatherWarm = "warm"
  protected val weatherCold = "cold"

  protected val eventRelax = "relax"
  protected val eventCelebrate = "celebrate"
  protected val eventFashion = "celebrate"

  protected val colorBase = "base"
  protected val styleSport = "sport"

  def generate(look: LookGeneratorDTO): LookGeneratorDTO
}
