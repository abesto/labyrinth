package net.abesto.labyrinth.systems

import com.artemis.ComponentMapper
import com.artemis.io.SaveFileFormat
import com.artemis.managers.{TagManager, WorldSerializationManager}
import net.abesto.labyrinth.components.{MazeComponent, PositionComponent}
import net.abesto.labyrinth.events.{LoadMazeEvent, MessageEvent}
import net.abesto.labyrinth.fsm.InStates
import net.abesto.labyrinth.fsm.States.{EditorState, GameState}
import net.abesto.labyrinth.fsm.Transitions.NewGameEvent
import net.abesto.labyrinth.macros._
import net.abesto.labyrinth.maze._
import net.abesto.labyrinth.{Constants, Tiles}
import net.mostlyoriginal.api.event.common.EventSystem
import squidpony.squidmath.Coord


@InStates(Array(classOf[GameState], classOf[EditorState]))
@DeferredEventHandlerSystem
class MazeLoaderSystem extends LabyrinthBaseSystem {
  var helpers: Helpers = _
  var tagManager: TagManager = _
  var serializationManager: WorldSerializationManager = _
  var mazeMapper: ComponentMapper[MazeComponent] = _
  var positionMapper: ComponentMapper[PositionComponent] = _
  var eventSystem: EventSystem = _

  def tileCallback(k: Tiles.Kind, x: Int, y: Int): Unit = k match {
    case Tiles.Kind.Player =>
      positionMapper.get(tagManager.getEntityId(Constants.Tags.player)).coord = Coord.get(x, y)
    case _ =>
  }

  @SubscribeDeferred
  def load(e: LoadMazeEvent): Unit = {
    try {
      helpers.mazeComponent.maze = MazeBuilder.fromFile(e.name, tileCallback).hashesToLines().get
      serializationManager.load(getClass.getResourceAsStream(s"/maps/${e.name}.json"), classOf[SaveFileFormat])
    } catch {
      case exc: Exception =>
        logger.error(s"Failed to load map ${e.name}", exc)
        eventSystem.dispatch(MessageEvent(s"Failed to load map ${e.name}. Maybe misspelled?"))
    }
  }

  @SubscribeDeferred
  def newGame(e: NewGameEvent): Unit = {
    eventSystem.dispatch(LoadMazeEvent("1-dry"))
  }
}
