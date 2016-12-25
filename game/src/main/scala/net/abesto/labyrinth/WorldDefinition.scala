package net.abesto.labyrinth

import com.artemis._
import com.artemis.managers.TagManager
import net.abesto.labyrinth.components._
import net.abesto.labyrinth.render.Renderer
import net.abesto.labyrinth.systems._
import net.mostlyoriginal.api.event.common.EventSystem

object WorldDefinition {
  protected def buildWorldConfiguration(renderer: Renderer): WorldConfiguration = new WorldConfigurationBuilder()
    .`with`(
      new TagManager(),
      new EventSystem(),
      // Maze
      new MazeLoaderSystem(),
      new MazeGeneratorSystem(),
      // Player actions
      new MovementSystem(),
      // Consequences, feedback
      new ShallowWaterMakesWet(),
      new PopupTriggerSystem(),
      new ShadowcastingSystem(),
      renderer
    ).build()

  def buildWorld(renderer: Renderer): World = {
    val world = new World(buildWorldConfiguration(renderer))
    createMap(world)
    createPlayer(world)
    world
  }

  protected def createMap(world: World): Unit = {
    val maze = world.createEntity()
    maze.edit().add(new MazeComponent)
    world.getSystem(classOf[TagManager]).register(Constants.Tags.maze, maze)
  }

  protected def createPlayer(world: World): Unit = {
    val player = world.createEntity()
    world.getSystem(classOf[TagManager]).register(Constants.Tags.player, player)
    player.edit()
      .add(new PositionComponent())
      .add(new LayerComponent(LayerComponent.Layers.Creature))
      .add(new TileComponent(Tiles.Kind.Player))
  }
}
