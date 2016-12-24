package net.abesto.labyrinth

import com.badlogic.ashley.core.{Engine, Entity}
import net.abesto.labyrinth.components.{PlayerMarker, PositionComponent}
import net.abesto.labyrinth.maze.MazeComponent
import net.abesto.labyrinth.systems.{MazeGeneratorSystem, MazeLoaderSystem, MovementSystem, ShadowcastingSystem}

class World(engine: Engine) {
  createMap()
  createPlayer()
  engine.addSystem(new MazeLoaderSystem)
  engine.addSystem(new MazeGeneratorSystem)

  engine.addSystem(new MovementSystem)

  engine.addSystem(new ShadowcastingSystem)

  def createMap(): Unit = {
    val map = new Entity()
    map.add(MazeComponent(null))
    engine.addEntity(map)
  }

  def createPlayer(): Unit = {
    val player = new Entity
    player.add(PositionComponent(0, 0))
    player.add(new PlayerMarker)
    engine.addEntity(player)
  }
}
