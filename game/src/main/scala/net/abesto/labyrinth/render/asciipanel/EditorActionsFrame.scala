package net.abesto.labyrinth.render.asciipanel

import java.awt.Color

import asciiPanel.AsciiPanel
import net.abesto.labyrinth.Constants
import net.abesto.labyrinth.fsm.InStates
import net.abesto.labyrinth.fsm.States.EditorState
import net.abesto.labyrinth.systems.Helpers

@InStates(Array(classOf[EditorState]))
class EditorActionsFrame(topLeftX: Int, topLeftY: Int, width: Int, height: Int) extends Frame(topLeftX, topLeftY, width, height) {
  var helpers: Helpers = _

  val marginLeft = 3
  val marginTop = 3
  val lineHeight = 2

  override def render(): Unit = {
    clear(Color.darkGray)
    clear(topLeft.add(1), bottomRight.subtract(1), Color.black)
    Constants.editorActions.getOrElse(helpers.state.current.asInstanceOf[EditorState], Seq.empty).zipWithIndex.foreach {
      case (action, idx) =>
        write(helpers.maze.tileset.toChar(action.tile), marginLeft, marginTop + idx * lineHeight, AsciiPanel.brightWhite, AsciiPanel.black)
        write(s": ${action.description}", marginLeft + 1, marginTop + idx * lineHeight, AsciiPanel.white, AsciiPanel.black)
    }
  }
}
