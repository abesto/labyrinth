package net.abesto.labyrinth.systems

import com.artemis.BaseSystem
import net.abesto.labyrinth.Constants
import net.abesto.labyrinth.components.MazeHighlightComponent.Type.EditorMazeCursor
import net.abesto.labyrinth.events.EditorMoveMazeCursorEvent
import net.abesto.labyrinth.fsm.InStates
import net.abesto.labyrinth.fsm.States.EditorState
import net.abesto.labyrinth.fsm.Transitions.{CloseEditorEvent, OpenEditorEvent}
import net.abesto.labyrinth.macros.{DeferredEventHandlerSystem, SubscribeDeferred}
import squidpony.squidmath.Coord

@InStates(Array(classOf[EditorState]))
@DeferredEventHandlerSystem
class EditorSystem extends BaseSystem {
  var helpers: Helpers = _

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
}
