package net.abesto.labyrinth

import com.badlogic.ashley.core.{ComponentMapper, Engine, Entity, Family}
import net.abesto.labyrinth.components.PlayerMarker
import net.abesto.labyrinth.maze.MazeComponent
import net.abesto.labyrinth.systems.MazeLoaderSystem

object EngineAccessors {
  val mapComponentMapper: ComponentMapper[MazeComponent] = ComponentMapper.getFor(classOf[MazeComponent])

  def getSingle(e: Engine, family: Family.Builder): Entity = {
    val es = e.getEntitiesFor(family.get)
    assert(es.size == 1)
    es.get(0)
  }

  def mapEntity(e: Engine): Entity = getSingle(e, Family.all(classOf[MazeComponent]))

  def map(e: Engine): MazeComponent = {
     mapComponentMapper.get(mapEntity(e))
  }

  def loadMap(e: Engine, name: String): Unit = {
    e.getSystem(classOf[MazeLoaderSystem]).load(name)
  }

  def player(e: Engine): Entity = getSingle(e, Family.all(classOf[PlayerMarker]))
}
