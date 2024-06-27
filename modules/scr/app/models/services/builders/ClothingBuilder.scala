package models.services.builders

import models.dao.entities.ClothingItem
import models.dao.repositories.CrudRepository
import models.dto.LookGeneratorDTO
import org.squeryl.dsl.ast.LogicalBoolean
import org.squeryl.PrimitiveTypeMode._

import scala.util.Random

trait ClothingBuilder[Entity <: ClothingItem, Repository <: CrudRepository[String, Entity]] extends Builder [Entity, Repository] {

  protected val lengthMini = "mini"
  protected val lengthMid = "mid"
  protected val lengthMidi = "midi"
  protected val lengthMax = "max"
  protected val lengthHip = "hip"

  private def noWeirdFilter(): ClothingItem => LogicalBoolean = _.isWeird === false
  private def baseColorFilter(): ClothingItem => LogicalBoolean = _.color === "base"
  private def sportStyleFilter(): ClothingItem => LogicalBoolean = _.style === "sport"
  private def highFashionabilityFilter(): ClothingItem => LogicalBoolean = _.fashionability gte 50
  private def fashionabilityFilter(): ClothingItem => LogicalBoolean = _.fashionability gt 0

  protected def checkIn(look: LookGeneratorDTO): List[ClothingItem => LogicalBoolean] = {
    (look.hasWeirdElement, look.hasColor) match {
      case (true, true) => List(noWeirdFilter(), baseColorFilter())
      case (true, false) => List(noWeirdFilter())
      case (false, true) => List(baseColorFilter())
      case _ => Nil
    }
  }

  protected def checkOut(look: LookGeneratorDTO, lookPiece: Option[ClothingItem]): LookGeneratorDTO = lookPiece match {
    case Some(x) =>
      if (x.isWeird) look.hasWeirdElement = true
      if (x.color != "base") look.hasColor = true
      look
    case _ => look
  }

  protected def getFiltersByEvent(event: String): List[ClothingItem => LogicalBoolean] = {
    val noWeird = if ((event == "celebrate") || event == "fashion") List(noWeirdFilter()) else Nil

    val fashionFilter = if (event == "fashion") {
      if (Random.nextInt(3) == 2) {
        List(highFashionabilityFilter())
      } else {
        List(fashionabilityFilter())
      }
    } else Nil

    List(noWeird, fashionFilter).flatten
  }

  protected def getFilterByRelaxEvent(event: String): List[ClothingItem => LogicalBoolean] = {
    if (event == "relax" && Random.nextInt(3) == 2) {
      List(sportStyleFilter())
    } else {
      List.empty
    }
  }
}
