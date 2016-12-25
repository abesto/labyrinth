package net.abesto.labyrinth

import com.artemis.{Aspect, World}

import scala.collection.immutable.IndexedSeq

object Helpers {
  def entityIdsOfAspect(world: World, aspectBuilder: Aspect.Builder): IndexedSeq[Int] = {
    val entityIdsBag = world.getAspectSubscriptionManager.get(aspectBuilder).getEntities
    0.until(entityIdsBag.size).map(entityIdsBag.get)
  }
}
