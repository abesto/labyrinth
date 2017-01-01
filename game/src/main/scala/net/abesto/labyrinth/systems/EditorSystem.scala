package net.abesto.labyrinth.systems

import net.abesto.labyrinth.Constants
import net.abesto.labyrinth.components.MazeHighlightComponent.Type.EditorMazeCursor
import net.abesto.labyrinth.events.{EditorChangeTileEvent, EditorGenerateMazeEvent, EditorMoveMazeCursorEvent, GenerateMazeEvent}
import net.abesto.labyrinth.fsm.InStates
import net.abesto.labyrinth.fsm.States.EditorState
import net.abesto.labyrinth.fsm.Transitions.{CloseEditorEvent, OpenEditorEvent}
import net.abesto.labyrinth.macros.{DeferredEventHandlerSystem, SubscribeDeferred}
import net.abesto.labyrinth.maze.MazeTile
import net.mostlyoriginal.api.event.common.EventSystem
import squidpony.squidmath.Coord

@InStates(Array(classOf[EditorState]))
@DeferredEventHandlerSystem
class EditorSystem extends InstrumentedSystem {
  var helpers: Helpers = _
  var eventSystem: EventSystem = _

  @SubscribeDeferred
  def openEditor(e: OpenEditorEvent): Unit = {
    helpers.highlight.set(EditorMazeCursor, Seq(Coord.get(Constants.mazeWidth / 2, Constants.mazeHeight / 2)))
  }

  @SubscribeDeferred
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
  def changeTile(e: EditorChangeTileEvent[_ <: MazeTile]): Unit = {
    helpers.highlight.get(EditorMazeCursor).foreach(
      coord => helpers.maze.update(e.makeTile(coord.getX, coord.getY))
    )
  }
}
