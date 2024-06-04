package models.services.builders.decorator

import com.google.inject.Inject
import models.dao.entities.Makeup
import models.dao.repositories. MakeupRepositoryImpl
import models.dto.Look
import scala.util.Random

trait MakeupBuilder extends DecoratorBuilder[Makeup, MakeupRepositoryImpl]

class MakeupBuilderImpl @Inject()(val makeupRepository: MakeupRepositoryImpl) extends MakeupBuilder {

    override def generate(look: Look, queryFilters: Map[String, Seq[String]]): Look = {
        if (Random.nextInt(3) == 2 || queryFilters("event").head == "celebrate") {
            val filters = getFilters(look, queryFilters)
            val makeup: Option[Makeup] = getElementFromDatabase(filters)(makeupRepository)

            if (makeup.isEmpty) {
                throw new Exception("No makeup was found")
            } else {
                look.makeup = makeup
                checkOut(look, look.makeup)
            }
        } else {
            look
          }
    }
}