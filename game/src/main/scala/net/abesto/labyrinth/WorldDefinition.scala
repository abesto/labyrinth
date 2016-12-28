package net.abesto.labyrinth

import com.artemis._
import com.artemis.io.JsonArtemisSerializer
import com.artemis.managers.{TagManager, WorldSerializationManager}
import net.abesto.labyrinth.components._
import net.abesto.labyrinth.magic.{AreaTarget, SpellParser, SpellWordList, TestEffect}
import net.abesto.labyrinth.render.Renderer
import net.abesto.labyrinth.systems._
import net.mostlyoriginal.api.event.common.EventSystem
import squidpony.squidmath.Coord

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
      new SpellInputSystem(new SpellParser(buildSpellWordList())),
      // Consequences, feedback
      new SpellCastingSystem(),
      new ShallowWaterMakesWet(),
      new PopupTriggerSystem(),
      new ShadowcastingSystem(),
      new InputMapManager(),
      // Finally, render
      renderer
    ).build()

  protected def buildSpellWordList(): SpellWordList = new SpellWordList(Seq(
    new TestEffect().withString("test"),
    AreaTarget(Coord.get(0, 0), Coord.get(1, 1)).withString("me"),
    AreaTarget(Coord.get(1, 0), Coord.get(1, 1)).withString("east"),
    AreaTarget(Coord.get(-1, 0), Coord.get(1, 1)).withString("west"),
    AreaTarget(Coord.get(0, -1), Coord.get(1, 1)).withString("north"),
    AreaTarget(Coord.get(0, 1), Coord.get(1, 1)).withString("south")
  ))

  def buildWorld(renderer: Renderer): World = {
    val world = new World(buildWorldConfiguration(renderer))
    val manager = world.getSystem(classOf[WorldSerializationManager])
    val serializer = new JsonArtemisSerializer(world).prettyPrint(true)
    manager.setSerializer(serializer.asInstanceOf[WorldSerializationManager.ArtemisSerializer[_]])
    Helpers.setSerializer(serializer)
    createMap(world)
    createPlayer(world)
    createSpellInput(world)
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

  protected def createSpellInput(world: World): Unit = {
    val entity = world.createEntity()
    world.getSystem(classOf[TagManager]).register(Constants.Tags.spellInput, entity)
    entity.edit().add(new SpellInputComponent)
  }
}
