package net.abesto.labyrinth.systems

import java.io._
import java.text.SimpleDateFormat
import java.util.Date

import asciiPanel.AsciiPanel
import com.artemis.annotations.AspectDescriptor
import com.artemis.io.SaveFileFormat
import com.artemis.managers.WorldSerializationManager
import com.artemis.{Aspect, ComponentMapper}
import net.abesto.labyrinth.components.MazeHighlightComponent.Type.EditorMazeCursor
import net.abesto.labyrinth.components.{PersistInMazeMarker, PositionComponent}
import net.abesto.labyrinth.events._
import net.abesto.labyrinth.fsm.InStates
import net.abesto.labyrinth.fsm.States.EditorState
import net.abesto.labyrinth.fsm.Transitions._
import net.abesto.labyrinth.macros.{DeferredEventHandlerSystem, SubscribeDeferred}
import net.abesto.labyrinth.{Constants, Tiles}
import net.mostlyoriginal.api.event.common.{EventSystem, Subscribe}
import squidpony.squidmath.Coord

@InStates(Array(classOf[EditorState]))
@DeferredEventHandlerSystem
class EditorSystem extends LabyrinthBaseSystem {
  var helpers: Helpers = _
  var eventSystem: EventSystem = _
  var mazeLoaderSystem: MazeLoaderSystem = _
  var positionMapper: ComponentMapper[PositionComponent] = _
  var serialization: WorldSerializationManager = _

  // Transient state
  var filename: Option[String] = None
  var modified: Boolean = false

  val dateFormat = new SimpleDateFormat("HH:mm:ss")
  def date: String = dateFormat.format(new Date())
  def withDate(msg: String): String = s"[$date] $msg"

  def error(msg: String): Unit = {
    helpers.prompt.error(msg)
    eventSystem.dispatch(MessageEvent(withDate(msg)))
  }

  def mode(msg: String): Unit = {
    helpers.prompt.reset()
    helpers.prompt.fgColor = AsciiPanel.yellow
    helpers.prompt.prompt = s"--$msg--"
  }

  def status(msg: String): Unit = {
    helpers.prompt.reset()
    helpers.prompt.prompt = msg
    val msgWithDate = withDate(msg)
    eventSystem.dispatch(MessageEvent(msgWithDate))
    logger.info(msgWithDate)
  }

  @AspectDescriptor(all=Array(classOf[PersistInMazeMarker]))
  var entitiesToSerialize: Aspect.Builder = _

  @Subscribe
  def openEditor(e: OpenEditorEvent): Unit = {
    helpers.highlight.set(EditorMazeCursor, Seq(Coord.get(Constants.mazeWidth / 2, Constants.mazeHeight / 2)))
    filename = None
    modified = false
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
    status("Generated new maze")
    modified = true
  }

  @SubscribeDeferred
  def enterTileEditor(e: OpenTileEditorEvent): Unit = {
    mode("TILE")
  }

  @SubscribeDeferred
  def changeTile(e: EditorChangeTileEvent): Unit = {
    helpers.highlight.get(EditorMazeCursor).foreach(
      coord => helpers.maze.update(coord.getX, coord.getY, e.kind)
    )
    modified = true
  }

  @SubscribeDeferred
  def leaveTileEditor(e: CloseTileEditorEvent): Unit = {
    helpers.prompt.reset()
  }

  @SubscribeDeferred
  def enterExMode(e: EditorOpenExtendedModeEvent): Unit = {
    helpers.prompt.reset()
    helpers.prompt.prompt = ":"
  }

  @SubscribeDeferred
  def handleExModeCommand(e: EditorExecuteExtendedModeEvent): Unit = {
    val parts = helpers.prompt.input.split(" +", 2)
    val arg = parts.tail.headOption
    parts.head match {
      case "e" =>
        if (arg.isEmpty) {
          error("E32: no file name")
          return
        }
        filename = arg
        modified = false
        if (mazeLoaderSystem.exists(filename.get)) {
          modified = false
          eventSystem.dispatch(LoadMazeEvent(filename.get))
        } else {
          status('"' + filename.get + '"' + " [New File]")
          modified = true
        }
      case "q" => eventSystem.dispatch(new CloseEditorEvent)
      case "w" =>
        if (arg.isDefined) {
          filename = arg
        }
        if (filename.isEmpty) {
          error("E32: No file name")
          return
        }
        // Write the maze layout
        val mazePw = new PrintWriter(filename.get)
        mazePw.write(
          helpers.maze.withTileset(Tiles.squidlib,
            _.tiles.transpose.map(
              _.map(_.char.character).mkString
            ).mkString("\n")
          )
        )
        mazePw.close()
        // Write entities
        val entityIds = helpers.entityIds(entitiesToSerialize)
        val jsonOutputStream = new BufferedOutputStream(new FileOutputStream(s"${filename.get}.json"))
        serialization.save(
          jsonOutputStream,
          new SaveFileFormat(entityIds)
        )
        jsonOutputStream.close()
        // Notify user
        status(
          '"' + filename.get + '"' + s" ${helpers.maze.width}x${helpers.maze.height} maze, ${entityIds.size()} entities written"
        )
        modified = false
      case cmd =>
        error(s"E492: Not an editor command: $cmd")
    }
  }

  @SubscribeDeferred
  def exModeAborted(e: EditorAbortExtendedModeEvent): Unit = {
    helpers.prompt.reset()
  }

  @Subscribe
  def opened(e: EditorMazeLoadedEvent): Unit = {
    status(
      '"' + e.path + '"' + s" ${e.maze.width}x${e.maze.height} maze, ${e.entityCount} entities"
    )
  }

  @SubscribeDeferred
  def setPlayerPosition(e: EditorSetPlayerPositionEvent): Unit = {
    val pos = helpers.highlight.get(EditorMazeCursor).ensuring(_.length == 1).head
    val posComponent = positionMapper.get(helpers.playerEntityId)
    if (!posComponent.coord.equals(pos)) {
      positionMapper.get(helpers.playerEntityId).coord = pos
      status(s"Set player starting position to $pos")
      modified = true
    }
  }
}
