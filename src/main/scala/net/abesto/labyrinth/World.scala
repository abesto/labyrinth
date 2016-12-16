package net.abesto.labyrinth

import com.badlogic.ashley.core.{Engine, Entity}
import net.abesto.labyrinth.components.{ActionQueueComponent, PlayerMarker, PositionComponent}
import net.abesto.labyrinth.map.MapComponent
import net.abesto.labyrinth.systems.{ActionQueueSystem, MapLoaderSystem, MazeGeneratorSystem, ShadowcastingSystem}

class World(engine: Engine) {
  createMap()
  createPlayer()
  engine.addSystem(new ActionQueueSystem)
  engine.addSystem(new MapLoaderSystem)
  engine.addSystem(new MazeGeneratorSystem)
  engine.addSystem(new ShadowcastingSystem)

  def createMap(): Unit = {
    val map = new Entity()
    map.add(new MapComponent)
    engine.addEntity(map)
  }

  def createPlayer(): Unit = {
    val player = new Entity
    player.add(new ActionQueueComponent())
    player.add(PositionComponent(0, 0))
    player.add(new PlayerMarker)
    engine.addEntity(player)
  }
}
