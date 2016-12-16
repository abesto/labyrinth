package net.abesto.labyrinth

import com.badlogic.ashley.core.{Engine, Entity}
import net.abesto.labyrinth.components.{ActionQueueComponent, PositionComponent}
import net.abesto.labyrinth.map.MapComponent
import net.abesto.labyrinth.systems.ActionQueueSystem

class World(engine: Engine) {
  val map = createMap()
  val player = createPlayer()
  engine.addSystem(new ActionQueueSystem)

  def createMap(): Unit = {
    val map = new Entity()
    map.add(new MapComponent)
    engine.addEntity(map)
    map
  }

  def createPlayer(): Entity = {
    val player = new Entity
    player.add(new ActionQueueComponent())
    player.add(PositionComponent(10, 10))
    engine.addEntity(player)
    player
  }
}
