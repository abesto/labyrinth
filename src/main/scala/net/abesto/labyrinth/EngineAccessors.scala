package net.abesto.labyrinth

import com.badlogic.ashley.core.{ComponentMapper, Engine, Entity, Family}
import net.abesto.labyrinth.components.PlayerMarker
import net.abesto.labyrinth.map.MapComponent
import net.abesto.labyrinth.systems.MapLoaderSystem

object EngineAccessors {
  val mapComponentMapper = ComponentMapper.getFor(classOf[MapComponent])

  def getSingle(e: Engine, family: Family.Builder): Entity = {
    val es = e.getEntitiesFor(family.get)
    assert(es.size == 1)
    es.get(0)
  }

  def mapEntity(e: Engine): Entity = getSingle(e, Family.all(classOf[MapComponent]))

  def map(e: Engine): MapComponent = {
     mapComponentMapper.get(mapEntity(e))
  }

  def loadMap(e: Engine, name: String): Unit = {
    e.getSystem(classOf[MapLoaderSystem]).load(name)
  }

  def player(e: Engine): Entity = getSingle(e, Family.all(classOf[PlayerMarker]))
}
