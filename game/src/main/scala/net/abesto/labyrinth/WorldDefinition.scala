package net.abesto.labyrinth

import com.artemis._
import com.artemis.io.JsonArtemisSerializer
import com.artemis.managers.{TagManager, WorldSerializationManager}
import com.esotericsoftware.jsonbeans.{Json, JsonSerializer, JsonValue}
import net.abesto.labyrinth.Tiles.Kind.Book
import net.abesto.labyrinth.components._
import net.abesto.labyrinth.render.Renderer
import net.abesto.labyrinth.systems._
import net.mostlyoriginal.api.event.common.EventSystem

object WorldDefinition {
  protected def buildWorldConfiguration(renderer: Renderer): WorldConfiguration = new WorldConfigurationBuilder()
    .`with`(
      new WorldSerializationManager(),
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
      // Input map handling
      new PopupInputMapSystem(),
      // Finally, render
      renderer
    ).build()

  def buildWorld(renderer: Renderer): World = {
    val world = new World(buildWorldConfiguration(renderer))
    val manager = world.getSystem(classOf[WorldSerializationManager])
    val serializer = new JsonArtemisSerializer(world).prettyPrint(true)
    manager.setSerializer(serializer.asInstanceOf[WorldSerializationManager.ArtemisSerializer[_]])
    Helpers.setSerializer(serializer)
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
      .add(new LayerComponent(LayerComponent.Layer.Creature))
      .add(new TileComponent(Tiles.Kind.Player))
  }
}
