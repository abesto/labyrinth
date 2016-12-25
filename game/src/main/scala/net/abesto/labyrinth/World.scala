package net.abesto.labyrinth

import com.badlogic.ashley.core.{Engine, Entity}
import net.abesto.labyrinth.components.{LayerComponent, PlayerMarker, PositionComponent, TileComponent}
import net.abesto.labyrinth.maze.MazeComponent
import net.abesto.labyrinth.systems._

class World(engine: Engine) {
  // Player, Maze
  createMap()
  createPlayer()
  engine.addSystem(new MazeLoaderSystem)
  engine.addSystem(new MazeGeneratorSystem)

  // Player actions
  engine.addSystem(new MovementSystem)

  // Consequences of player actions
  engine.addSystem(new ShallowWaterMakesWet)

  // Rendering, feedback
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
    player.add(LayerComponent(LayerComponent.Layers.Creature))
    player.add(TileComponent(Tiles.Kind.Player))
    engine.addEntity(player)
  }
}
