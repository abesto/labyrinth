package net.abesto.labyrinth.systems

import net.abesto.labyrinth.Constants
import net.abesto.labyrinth.components.MazeHighlightComponent.Type.EditorMazeCursor
import net.abesto.labyrinth.events._
import net.abesto.labyrinth.fsm.InStates
import net.abesto.labyrinth.fsm.States.EditorState
import net.abesto.labyrinth.fsm.Transitions.{CloseEditorEvent, EditorExecuteOpenMazeEvent, EditorOpenMazeEvent, OpenEditorEvent}
import net.abesto.labyrinth.macros.{DeferredEventHandlerSystem, SubscribeDeferred}
import net.mostlyoriginal.api.event.common.{EventSystem, Subscribe}
import squidpony.squidmath.Coord

@InStates(Array(classOf[EditorState]))
@DeferredEventHandlerSystem
class EditorSystem extends LabyrinthBaseSystem {
  var helpers: Helpers = _
  var eventSystem: EventSystem = _

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
  def openPrompt(e: EditorOpenMazeEvent): Unit = {
    helpers.prompt.prompt = "Open maze"
  }

  @SubscribeDeferred
  def open(e: EditorExecuteOpenMazeEvent): Unit = {
    eventSystem.dispatch(LoadMazeEvent(helpers.prompt.input))
    helpers.prompt.reset()
  }
}
