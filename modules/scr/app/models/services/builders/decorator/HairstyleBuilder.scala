package models.services.builders.decorator

import com.google.inject.Inject
import models.dao.entities.Hairstyle
import models.dao.repositories.HairstyleRepositoryImpl
import models.dto.Look
import org.squeryl.dsl.ast.LogicalBoolean
import org.squeryl.PrimitiveTypeMode._

import scala.util.Random

trait HairstyleBuilder extends DecoratorBuilder[Hairstyle, HairstyleRepositoryImpl]

class HairstyleBuilderImpl @Inject()(val hairstyleRepository: HairstyleRepositoryImpl) extends HairstyleBuilder {

    private def highStylingFilter(): Hairstyle => LogicalBoolean = _.stylingDegree gt 8
    private def noHighStylingFilter(): Hairstyle => LogicalBoolean = _.stylingDegree lt 8
    private def someStylingFilter(): Hairstyle => LogicalBoolean = _.stylingDegree gt 0

    override def generate(look: Look, queryFilters: Map[String, Seq[String]]): Look = {
        if (Random.nextInt(3) == 2 || queryFilters("tailDay").head != "low") {
            val filters = getFilters(look, queryFilters)
            val hairstyle: Option[Hairstyle] = getElementFromDatabase(filters)(hairstyleRepository)

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

    override def getFilters(look: Look, queryFilters: Map[String, Seq[String]]): List[Hairstyle => LogicalBoolean] = {
        getFiltersByTailDay(queryFilters("tailDay").head) ::: checkIn(look)
    }

    private def getFiltersByTailDay(tailDay: String): List[Hairstyle => LogicalBoolean] = tailDay match {
        case "high" => List(highStylingFilter())
        case "medium" => List(noHighStylingFilter(), someStylingFilter())
        case _ => List.empty
    }
}