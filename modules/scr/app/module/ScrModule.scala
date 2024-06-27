package module

import di.AppModule
import models.dao.repositories._
import models.services.builders._
import models.services.builders.decorator._
import models.services.{LookBookService, LookBookServiceImpl}

class ScrModule extends AppModule{
  override def configure(): Unit = {
    bindSingleton[LookBookService, LookBookServiceImpl]
    bindSingleton[TopRepository, TopRepositoryImpl]
    bindSingleton[BottomRepository, BottomRepositoryImpl]
    bindSingleton[ShoesRepository, ShoesRepositoryImpl]
    bindSingleton[LookRepository, LookRepositoryImpl]
    bindSingleton[TopClothingBuilder, TopClothingBuilderImpl]
    bindSingleton[CoatingClothingBuilder, CoatingClothingBuilderImpl]
    bindSingleton[BottomClothingBuilder, BottomClothingBuilderImpl]
    bindSingleton[ShoesClothingBuilder, ShoesClothingBuilderImpl]
    bindSingleton[HairstyleBuilder, HairstyleBuilderImpl]
    bindSingleton[HairstyleRepository, HairstyleRepositoryImpl]
    bindSingleton[MakeupBuilder, MakeupBuilderImpl]
    bindSingleton[MakeupRepository, MakeupRepositoryImpl]
    bindSingleton[JewelryBuilder, JewelryBuilderImpl]
    bindSingleton[JewelryRepository, JewelryRepositoryImpl]
  }
}
