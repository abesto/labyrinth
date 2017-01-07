package net.abesto.labyrinth.systems

import com.artemis.io.SaveFileFormat
import com.artemis.managers.{TagManager, WorldSerializationManager}
import net.abesto.labyrinth.events.{LoadMazeEvent, MessageEvent}
import net.abesto.labyrinth.fsm.InStates
import net.abesto.labyrinth.fsm.States.{EditorState, GameState}
import net.abesto.labyrinth.fsm.Transitions.NewGameEvent
import net.abesto.labyrinth.macros._
import net.abesto.labyrinth.maze._
import net.mostlyoriginal.api.event.common.EventSystem


@InStates(Array(classOf[GameState], classOf[EditorState]))
@DeferredEventHandlerSystem
class MazeLoaderSystem extends LabyrinthBaseSystem {
  var helpers: Helpers = _
  var tagManager: TagManager = _
  var serializationManager: WorldSerializationManager = _
  var eventSystem: EventSystem = _

  @SubscribeDeferred
  def load(e: LoadMazeEvent): Unit = {
    try {
      helpers.mazeComponent.maze = MazeBuilder.fromFile(e.name).hashesToLines().get
      world.delete(helpers.playerEntityId)
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
