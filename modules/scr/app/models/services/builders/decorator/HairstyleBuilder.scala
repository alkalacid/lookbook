package models.services.builders.decorator

import com.google.inject.Inject
import models.dao.entities.Hairstyle
import models.dao.repositories.HairstyleRepositoryImpl
import models.dto.LookGeneratorDTO
import org.squeryl.dsl.ast.LogicalBoolean
import org.squeryl.PrimitiveTypeMode._

import scala.util.Random

trait HairstyleBuilder extends DecoratorBuilder[Hairstyle, HairstyleRepositoryImpl]

class HairstyleBuilderImpl @Inject()(val repository: HairstyleRepositoryImpl) extends HairstyleBuilder {

    val builderName: String = "hairstyle"

    private val tailDayLow = "low"
    private val tailDayMedium = "medium"
    private val tailDayHigh = "high"

    private def highStylingFilter(): Hairstyle => LogicalBoolean = _.stylingDegree gt 8
    private def noHighStylingFilter(): Hairstyle => LogicalBoolean = _.stylingDegree lt 8
    private def someStylingFilter(): Hairstyle => LogicalBoolean = _.stylingDegree gt 0

    override def generate(look: LookGeneratorDTO, itemId: String): LookGeneratorDTO = {
        if (Random.nextInt(3) == 2 || look.tailDay != tailDayLow || itemId.nonEmpty) {
            val hairstyle: Option[Hairstyle] = getItem(look, itemId, repository)

            if (hairstyle.isEmpty) {
                throw new Exception("No hairstyle was found")
            } else {
                look.hairstyle = hairstyle
                checkOut(look, look.hairstyle)
            }
        } else {
            look
        }
    }

    override def getFilters(look: LookGeneratorDTO): List[Hairstyle => LogicalBoolean] = {
        List(getFiltersByTailDay(look.tailDay), checkIn(look)).flatten
    }

    private def getFiltersByTailDay(tailDay: String): List[Hairstyle => LogicalBoolean] = tailDay match {
        case this.tailDayHigh => List(highStylingFilter())
        case this.tailDayMedium => List(noHighStylingFilter(), someStylingFilter())
        case _ => List.empty
    }
}