package net.abesto.labyrinth

import com.artemis._
import com.artemis.io.JsonArtemisSerializer
import com.artemis.managers.{TagManager, WorldSerializationManager}
import net.abesto.labyrinth.components._
import net.abesto.labyrinth.magic._
import net.abesto.labyrinth.render.Renderer
import net.abesto.labyrinth.systems._
import net.mostlyoriginal.api.event.common.EventSystem
import squidpony.squidmath.Coord

object WorldDefinition {
  protected def worldConfiguration(renderer: Renderer): WorldConfiguration = new WorldConfigurationBuilder()
    .`with`(
      // Artemis base systems
      new StateTransitionSystem(),
      new WorldSerializationManager(),
      new TagManager(),
      // Custom base systems
      new EventSystem(),
      new Helpers(),
      new EntityFactory(),
      // Handle user input
      new InputHandlerSystem(),
      new MainMenuSystem(),
      new PromptSystem(),
      // The Level Editor
      new EditorSystem(),
      new MazeGeneratorSystem(),
      // -- GAME SYSTEMS -- //
      new MazeLoaderSystem(),
      // Player actions
      new MovementSystem(),
      new SpellInputSystem(new SpellParser(spellWordList())),
      // Consequences, feedback
      new SpellCastingSystem(),
      new ShallowWaterMakesWet(),
      new PopupTriggerSystem(),
      new ShadowcastingSystem(),
      // -- END OF GAME SYSTEMS -- //
      renderer
    ).build()

  protected def spellWordList(): SpellWordList = new SpellWordList(Seq(
    new TestEffect().withString("test"),
    new DryEffect().withString("dry"),
    AreaTarget(Coord.get(0, 0), Coord.get(1, 1)).withString("me"),
    AreaTarget(Coord.get(1, 0), Coord.get(1, 1)).withString("east"),
    AreaTarget(Coord.get(-1, 0), Coord.get(1, 1)).withString("west"),
    AreaTarget(Coord.get(0, -1), Coord.get(1, 1)).withString("north"),
    AreaTarget(Coord.get(0, 1), Coord.get(1, 1)).withString("south")
  ))

  protected def maze(world: World): Unit = {
    val maze = world.createEntity()
    maze.edit()
      .add(new MazeComponent)
      .add(new MazeHighlightComponent)
    world.getSystem(classOf[TagManager]).register(Constants.Tags.maze, maze)
  }

  protected def prompt(world: World): Unit = {
    val entity = world.createEntity()
    world.getSystem(classOf[TagManager]).register(Constants.Tags.prompt, entity)
    entity.edit().add(new PromptComponent)
  }

  protected def state(world: World): Unit = {
    val entity = world.createEntity()
    world.getSystem(classOf[TagManager]).register(Constants.Tags.state, entity)
    entity.edit().add(new StateComponent)
  }

  def world(renderer: Renderer): World = {
    val world = new World(worldConfiguration(renderer))
    val serializer = new JsonArtemisSerializer(world).prettyPrint(true)
    world.getSystem(classOf[WorldSerializationManager]).setSerializer(serializer.asInstanceOf[WorldSerializationManager.ArtemisSerializer[_]])
    world.getSystem(classOf[Helpers]).setSerializer(serializer)
    maze(world)
    prompt(world)
    state(world)
    world
  }
}
