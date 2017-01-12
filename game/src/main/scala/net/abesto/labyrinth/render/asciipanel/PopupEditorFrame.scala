package net.abesto.labyrinth.render.asciipanel

import java.awt.Color

import net.abesto.labyrinth.components.PopupComponent
import net.abesto.labyrinth.fsm.InStates
import net.abesto.labyrinth.fsm.States.PopupEditorState
import net.abesto.labyrinth.systems.EditorSystem.CursorState
import net.abesto.labyrinth.systems.{EditorSystem, Helpers}
import squidpony.squidmath.Coord

@InStates(Array(classOf[PopupEditorState]))
class PopupEditorFrame extends PopupFrame {
  var helpers: Helpers = _
  var editorSystem: EditorSystem = _  // This is not very nice, could be decoupled by moving popup editor state into a singleton component
  private def state = editorSystem.popupEditorState

  override def popup: PopupComponent = state.popup

  def physicalCursorPosition: Coord = state.cursorState match {
    case CursorState.Title => Coord.get(titleLeft + titlePaddingSize + state.cursorPosition.x, 0)
    case CursorState.Text => Coord.get(state.cursorPosition.x + 2, state.cursorPosition.y + 2)
    case _ => Coord.get(-1, -1)  // For everything else, we'll directly handle highlighting
  }

  override def write(c: Char, x: Int, y: Int, fg: Color, bg: Color): Unit = {
    val realBg =
      if (physicalCursorPosition.equals(Coord.get(x, y))) {
        Color.white
      } else {
        bg
      }
    super.write(c, x, y, fg, realBg)
  }
}

