package net.abesto.labyrinth.render.asciipanel

import java.awt.Color

import asciiPanel.AsciiPanel
import net.abesto.labyrinth.{Constants, Tiles}
import net.abesto.labyrinth.fsm.InStates
import net.abesto.labyrinth.fsm.States.EditorState
import net.abesto.labyrinth.systems.{EditorSystem, Helpers}

@InStates(Array(classOf[EditorState]))
class EditorActionsFrame(topLeftX: Int, topLeftY: Int, width: Int, height: Int) extends Frame(topLeftX, topLeftY, width, height) {
  var helpers: Helpers = _
  var editorSystem: EditorSystem = _

  val marginLeft = 3
  val marginTop = 3
  val lineHeight = 2

  override def render(): Unit = {
    clearAndBorder()
    title()
    actions()
  }

  protected def actions(): Unit = {
    Constants.editorActions.getOrElse(helpers.state.current.asInstanceOf[EditorState], Seq.empty).zipWithIndex.foreach {
      case (action, idx) =>
        val tileString: String = action.tiles.map(Tiles.dwarfFortress.toChar).mkString
        write(tileString, marginLeft, marginTop + idx * lineHeight, AsciiPanel.brightWhite, Color.gray)
        write(s" ${action.description}", marginLeft + tileString.length, marginTop + idx * lineHeight, AsciiPanel.white, AsciiPanel.black)
    }
  }

  protected def clearAndBorder(): Unit = {
    clear(Color.darkGray)
    clear(topLeft.add(1), bottomRight.subtract(1), Color.black)
  }

  protected def title(): Unit = {
    val title = editorSystem.filename.getOrElse("Untitled")
    val titleLength = title.length
    val titleLeft = (width - titleLength) / 2
    val titleRight = titleLeft + titleLength
    write(title, titleLeft, 0, AsciiPanel.white, Color.darkGray)
    if (editorSystem.modified) {
      write('*', titleRight, 0, AsciiPanel.white, Color.darkGray)
    }
  }
}
