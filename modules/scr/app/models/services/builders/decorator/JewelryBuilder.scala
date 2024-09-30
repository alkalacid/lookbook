package models.services.builders.decorator

import com.google.inject.Inject
import models.dao.entities.Jewelry
import models.dao.repositories.JewelryRepositoryImpl
import models.dto.LookGeneratorDTO
import org.squeryl.dsl.ast.LogicalBoolean
import org.squeryl.PrimitiveTypeMode._

import scala.annotation.tailrec
import scala.util.Random

trait JewelryBuilder extends DecoratorBuilder[Jewelry, JewelryRepositoryImpl]

class JewelryBuilderImpl @Inject()(val jewelryRepository: JewelryRepositoryImpl) extends JewelryBuilder {

  private val areaHands = "hands"
  private val areaEars = "ears"

  private def noHandsJewelryFilter(): Jewelry => LogicalBoolean = _.area <> areaHands

  override def generate(look: LookGeneratorDTO, queryFilters: Map[String, Seq[String]]): LookGeneratorDTO = {
      if (Random.nextInt(2) == 1 || queryFilters("event").head == eventCelebrate) {
          val filters = getFilters(look, queryFilters)
          val queryList: List[Jewelry] = getJewelryFromDatabase(filters)

          if (queryList.isEmpty) {
            look
          } else if (queryFilters("event").head == eventRelax) {
            look.jewelry = List(queryList(1))
            look
          } else {

            look.jewelry = Random.nextInt(12) match {
              case i if i < 6 => filterJewelry(queryList, List(), 1)
              case i if i < 10 => filterJewelry(queryList, List(), 2)
              case 10 | 11 => filterJewelry(queryList, List(), 3)
            }

            look
          }
      } else {
          look
        }
  }

  override protected def getFilters(look: LookGeneratorDTO, queryFilters: Map[String, Seq[String]]): List[Jewelry => LogicalBoolean] = {
    if (look.top.get.isSleeve) {
      List(List(noHandsJewelryFilter()), checkIn(look)).flatten
    } else {
      checkIn(look)
    }
  }

  private def getJewelryFromDatabase(filters: List[Jewelry => LogicalBoolean]): List[Jewelry] = {
    if (filters.nonEmpty) {
      Random.shuffle(jewelryRepository.filter(filters = filters))
    } else {
      Random.shuffle(jewelryRepository.list())
    }
  }

  @tailrec
  private def filterJewelry(jewelryList: List[Jewelry], resultList: List[Jewelry], limit: Int, acc: Int = 1): List[Jewelry] = {
    if (resultList.length == limit) {
      resultList
    } else {
      val newItem: Jewelry = jewelryList(acc)
      val newAcc: Int = acc + 1
      if (resultList.exists(j => j.area == areaEars) && newItem.area == areaEars) {
        filterJewelry(jewelryList, resultList, limit, newAcc)
      } else {
        filterJewelry(jewelryList, newItem :: resultList, limit, newAcc)
      }
    }
  }
}