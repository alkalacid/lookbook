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

class JewelryBuilderImpl @Inject()(val repository: JewelryRepositoryImpl) extends JewelryBuilder {

  val builderName: String = "jewelry"

  private val areaHands = "hands"
  private val areaEars = "ears"

  private def noHandsJewelryFilter(): Jewelry => LogicalBoolean = _.area <> areaHands

  override def generate(look: LookGeneratorDTO, itemId: String): LookGeneratorDTO = {
      if (Random.nextInt(2) == 1 || look.event == eventCelebrate || itemId.nonEmpty) {
          val filters = getFilters(look)
          val queryList: List[Jewelry] = getJewelryFromDatabase(filters, itemId)

          if (queryList.isEmpty) {
            look
          } else if (look.event == eventRelax) {
            look.jewelry = List(queryList.head)
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

  override protected def getFilters(look: LookGeneratorDTO): List[Jewelry => LogicalBoolean] = {
    if (look.hasSleeves) {
      List(List(noHandsJewelryFilter()), checkIn(look)).flatten
    } else {
      checkIn(look)
    }
  }

  private def getJewelryFromDatabase(filters: List[Jewelry => LogicalBoolean], itemId: String): List[Jewelry] = {
    val queryList: List[Jewelry] = if (filters.nonEmpty) {
      Random.shuffle(repository.filter(filters = filters))
    } else {
      Random.shuffle(repository.list())
    }

    if (itemId.nonEmpty) {
      getPredefinedJewelryFromDatabase(queryList, itemId)
    } else {
      queryList
    }
  }

  private def getPredefinedJewelryFromDatabase(queryList: List[Jewelry], itemId: String): List[Jewelry] = {
    val firstItem = repository.find(itemId).getOrElse(throw new Exception("No predefined jewelry was found"))
    val otherItems: List[Jewelry] = queryList.filter(_.id != itemId)
    firstItem :: otherItems
  }

  @tailrec
  private def filterJewelry(jewelryList: List[Jewelry], resultList: List[Jewelry], limit: Int, acc: Int = 0): List[Jewelry] = {
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