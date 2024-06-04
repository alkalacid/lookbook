package models.services.builders.decorator

import com.google.inject.Inject
import models.dao.entities.Jewelry
import models.dao.repositories.JewelryRepositoryImpl
import models.dto.Look
import org.squeryl.dsl.ast.LogicalBoolean
import org.squeryl.PrimitiveTypeMode._

import scala.annotation.tailrec
import scala.util.Random

trait JewelryBuilder extends DecoratorBuilder[Jewelry, JewelryRepositoryImpl]

class JewelryBuilderImpl @Inject()(val jewelryRepository: JewelryRepositoryImpl) extends JewelryBuilder {

  private def noHandsJewelryFilter(): Jewelry => LogicalBoolean = _.area <> "hands"

  override def generate(look: Look, queryFilters: Map[String, Seq[String]]): Look = {
      if (Random.nextInt(2) == 1 || queryFilters("event").head == "celebrate") {
          val filters = getFilters(look, queryFilters)
          val queryList: List[Jewelry] = getJewelryFromDatabase(filters)

          if (queryList.isEmpty) {
            look
          } else if (queryFilters("event").head == "relax") {
            look.jewelry = List(queryList(1))
            look
          } else {

            look.jewelry = Random.nextInt(12) match {
              case i if i < 6 => filterJewelry(queryList, List(), 1)
              case i if i < 10 => filterJewelry(queryList, List(), 2)
              case 11 => filterJewelry(queryList, List(), 3)
            }

            look
          }
      } else {
          look
        }
  }

  override protected def getFilters(look: Look, queryFilters: Map[String, Seq[String]]): List[Jewelry => LogicalBoolean] = {
    var filters: List[Jewelry => LogicalBoolean] = checkIn(look)
    if (look.top.get.isSleeve) {
      filters ::= noHandsJewelryFilter()
    }
    filters
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
    if (resultList.length != limit) {
      val newItem: Jewelry = jewelryList(acc)
      val newAcc: Int = acc + 1
      if (resultList.exists(j => j.area == "ears") && newItem.area == "ears") {
        filterJewelry(jewelryList, resultList, limit, newAcc)
      } else {
        filterJewelry(jewelryList, newItem :: resultList, limit, newAcc)
      }
    } else {
      resultList
    }
  }
}