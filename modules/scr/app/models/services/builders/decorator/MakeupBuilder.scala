package models.services.builders.decorator

import com.google.inject.Inject
import models.dao.entities.Makeup
import models.dao.repositories. MakeupRepositoryImpl
import models.dto.LookGeneratorDTO
import scala.util.Random

trait MakeupBuilder extends DecoratorBuilder[Makeup, MakeupRepositoryImpl]

class MakeupBuilderImpl @Inject()(val repository: MakeupRepositoryImpl) extends MakeupBuilder {

  val builderName: String = "makeup"

  override def generate(look: LookGeneratorDTO, itemId: String): LookGeneratorDTO = {
      if (Random.nextInt(3) == 2 || look.event == eventCelebrate || itemId.nonEmpty) {
          val makeup: Option[Makeup] = getItem(look, itemId, repository)

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