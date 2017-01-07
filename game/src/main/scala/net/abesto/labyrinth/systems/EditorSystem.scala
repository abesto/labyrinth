package net.abesto.labyrinth.systems

import java.io._

import com.artemis.Aspect
import com.artemis.annotations.AspectDescriptor
import com.artemis.io.SaveFileFormat
import com.artemis.managers.WorldSerializationManager
import net.abesto.labyrinth.components.MazeHighlightComponent.Type.EditorMazeCursor
import net.abesto.labyrinth.components.PersistInMazeMarker
import net.abesto.labyrinth.events._
import net.abesto.labyrinth.fsm.InStates
import net.abesto.labyrinth.fsm.States.EditorState
import net.abesto.labyrinth.fsm.Transitions.{CloseEditorEvent, EditorExecuteExtendedModeEvent, EditorOpenExtendedModeEvent, OpenEditorEvent}
import net.abesto.labyrinth.macros.{DeferredEventHandlerSystem, SubscribeDeferred}
import net.abesto.labyrinth.{Constants, Tiles}
import net.mostlyoriginal.api.event.common.{EventSystem, Subscribe}
import squidpony.squidmath.Coord

@InStates(Array(classOf[EditorState]))
@DeferredEventHandlerSystem
class EditorSystem extends LabyrinthBaseSystem {
  var helpers: Helpers = _
  var eventSystem: EventSystem = _
  var serialization: WorldSerializationManager = _

  @AspectDescriptor(all=Array(classOf[PersistInMazeMarker]))
  var entitiesToSerialize: Aspect.Builder = _

  @SubscribeDeferred
  def openEditor(e: OpenEditorEvent): Unit = {
    helpers.highlight.set(EditorMazeCursor, Seq(Coord.get(Constants.mazeWidth / 2, Constants.mazeHeight / 2)))
  }

  @Subscribe
  def closeEditor(e: CloseEditorEvent): Unit = {
    helpers.highlight.clear(EditorMazeCursor)
  }

  @SubscribeDeferred
  def moveMazeCursor(e: EditorMoveMazeCursorEvent): Unit = {
    val newHighlights = helpers.highlight.get(EditorMazeCursor).map(e.op)
    if (newHighlights.forall(_.isWithin(Constants.mazeWidth, Constants.mazeHeight))) {
      helpers.highlight.set(EditorMazeCursor, newHighlights)
    }
  }

  @SubscribeDeferred
  def generateMaze(e: EditorGenerateMazeEvent): Unit = {
    eventSystem.dispatch(new GenerateMazeEvent)
  }

  @SubscribeDeferred
  def changeTile(e: EditorChangeTileEvent): Unit = {
    helpers.highlight.get(EditorMazeCursor).foreach(
      coord => helpers.maze.update(coord.getX, coord.getY, e.kind)
    )
  }

  @SubscribeDeferred
  def openPrompt(e: EditorOpenExtendedModeEvent): Unit = {
    helpers.prompt.prompt = ":"
  }

  def write(filename: String, content: String): Unit = {
  }

  @SubscribeDeferred
  def open(e: EditorExecuteExtendedModeEvent): Unit = {
    val parts = helpers.prompt.input.split(" ", 2)
    parts.head match {
      case "e" => eventSystem.dispatch(LoadMazeEvent(parts(1)))
      case "q" => eventSystem.dispatch(new CloseEditorEvent)
      case "w" =>
        val mazeName = parts(1)
        // Write the maze layout
        val mazePw = new PrintWriter(mazeName)
        mazePw.write(
          helpers.maze.withTileset(Tiles.squidlib,
            _.tiles.transpose.map(
              _.map(_.char.character).mkString
            ).mkString("\n")
          )
        )
        mazePw.close()
        // Write entities
        val jsonOutputStream = new BufferedOutputStream(new FileOutputStream(s"$mazeName.json"))
        serialization.save(
          jsonOutputStream,
          new SaveFileFormat(helpers.entityIds(entitiesToSerialize))
        )
        jsonOutputStream.close()
        // Notify user
        eventSystem.dispatch(MessageEvent(s"Saved ${parts(1)}"))
      case cmd => eventSystem.dispatch(MessageEvent(s"Unknown command: $cmd"))
    }
    helpers.prompt.reset()
  }
}
