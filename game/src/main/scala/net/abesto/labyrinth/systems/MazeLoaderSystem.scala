package net.abesto.labyrinth.systems

import java.io.{BufferedInputStream, File, FileInputStream}

import com.artemis.io.SaveFileFormat
import com.artemis.managers.{TagManager, WorldSerializationManager}
import net.abesto.labyrinth.events.{LoadMazeEvent, MazeLoadedEvent, MessageEvent}
import net.abesto.labyrinth.fsm.States.{EditorState, GameState}
import net.abesto.labyrinth.fsm.Transitions.NewGameEvent
import net.abesto.labyrinth.fsm.{InStates, States}
import net.abesto.labyrinth.macros._
import net.abesto.labyrinth.maze._
import net.mostlyoriginal.api.event.common.EventSystem

import scala.io.Source


@InStates(Array(classOf[GameState], classOf[EditorState]))
@DeferredEventHandlerSystem
class MazeLoaderSystem extends LabyrinthBaseSystem {
  var helpers: Helpers = _
  var tagManager: TagManager = _
  var serializationManager: WorldSerializationManager = _
  var eventSystem: EventSystem = _

  def path(filename: String): String =
    if (helpers.state.isActive[EditorState]) {
      filename
    } else {
      getClass.getResource(filename).getPath
    }

  def exists(filename: String): Boolean = new File(path(filename)).exists()

  @SubscribeDeferred
  def load(e: LoadMazeEvent): Unit = {
    try {
      if (exists(e.name)) {
        val actualPath: String = path(e.name)
        val maze = MazeBuilder.fromFile(actualPath).hashesToLines().get
        val oldPlayerEntityId = helpers.playerEntityId

        val inputStream = new BufferedInputStream(new FileInputStream(s"$actualPath.json"))
        val saveFileFormat = serializationManager.load(inputStream, classOf[SaveFileFormat])
        inputStream.close()

        world.delete(oldPlayerEntityId)
        helpers.mazeComponent.maze = maze

        eventSystem.dispatch(MazeLoadedEvent(actualPath, maze, saveFileFormat.entities.size))
      }
    } catch {
      case exc: Exception =>
        logger.error(s"Failed to load map ${e.name}", exc)
        eventSystem.dispatch(MessageEvent(s"Failed to load map ${e.name}. Maybe misspelled?"))
    }
  }

  @SubscribeDeferred
  def newGame(e: NewGameEvent): Unit = {
    eventSystem.dispatch(LoadMazeEvent(
      getClass.getResource("/maps/1-dry").getPath
    ))
  }
}
