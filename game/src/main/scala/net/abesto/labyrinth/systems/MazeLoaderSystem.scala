package net.abesto.labyrinth.systems

import java.io.{BufferedInputStream, File, FileInputStream}

import com.artemis.Aspect
import com.artemis.annotations.AspectDescriptor
import com.artemis.io.SaveFileFormat
import com.artemis.managers.{TagManager, WorldSerializationManager}
import net.abesto.labyrinth.components.PersistInMazeMarker
import net.abesto.labyrinth.events.{EditorMazeLoadedEvent, LoadMazeEvent, MessageEvent}
import net.abesto.labyrinth.fsm.InStates
import net.abesto.labyrinth.fsm.States.{EditorState, GameState}
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

  @AspectDescriptor(all=Array(classOf[PersistInMazeMarker]))
  var mazeEntitiesAspectBuilder: Aspect.Builder = _

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
        val maze = MazeBuilder.fromFile(actualPath).get

        val inputStream = new BufferedInputStream(new FileInputStream(s"$actualPath.json"))
        val saveFileFormat = serializationManager.load(inputStream, classOf[SaveFileFormat])
        inputStream.close()

        unload()
        helpers.mazeComponent.maze = maze
        if (helpers.state.isActive[EditorState]) {
          eventSystem.dispatch(EditorMazeLoadedEvent(actualPath, maze, saveFileFormat.entities.size))
        }
      }
    } catch {
      case exc: Exception =>
        logger.error(s"Failed to load map ${e.name}", exc)
        eventSystem.dispatch(MessageEvent(s"Failed to load map ${e.name}. Maybe misspelled?"))
    }
  }

  def unload(): Unit = {
    val mazeEntities = helpers.entityIdsSeq(mazeEntitiesAspectBuilder)
    mazeEntities.foreach(world.delete)
  }
}
