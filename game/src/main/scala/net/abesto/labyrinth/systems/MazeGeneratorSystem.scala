package net.abesto.labyrinth.systems

import com.artemis.ComponentMapper
import com.artemis.managers.TagManager
import net.abesto.labyrinth.Constants
import net.abesto.labyrinth.components.{MazeComponent, PositionComponent}
import net.abesto.labyrinth.events.GenerateMazeEvent
import net.abesto.labyrinth.fsm.Transitions.OpenEditorEvent
import net.abesto.labyrinth.macros._
import net.abesto.labyrinth.maze._
import net.mostlyoriginal.api.event.common.EventSystem
import squidpony.squidgrid.mapping.DungeonUtility

@DeferredEventHandlerSystem
class MazeGeneratorSystem extends LabyrinthBaseSystem {
  var helpers: Helpers = _
  var positionMapper: ComponentMapper[PositionComponent] = _
  var eventSystem: EventSystem = _
  var mazeLoaderSystem: MazeLoaderSystem = _
  var entityFactory: EntityFactory = _

  @SubscribeDeferred
  def generate(e: GenerateMazeEvent): Unit = {
    val builder = MazeBuilder.random().hashesToLines().smoothFloor()
    val startPos = new DungeonUtility().randomFloor(builder.get.chars)

    mazeLoaderSystem.unload()
    entityFactory.player(startPos)
    helpers.mazeComponent.maze = builder.roughFloor().get
  }

  @SubscribeDeferred
  def openEditor(e: OpenEditorEvent): Unit = {
    eventSystem.dispatch(new GenerateMazeEvent)
  }
}
