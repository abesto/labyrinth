package net.abesto.labyrinth.render.asciipanel

import java.awt.Color

import asciiPanel.AsciiPanel
import net.abesto.labyrinth.Constants
import net.abesto.labyrinth.fsm.States._
import net.abesto.labyrinth.fsm._
import net.abesto.labyrinth.systems.Helpers

@InStates(Array(classOf[MainMenuState]))
class MainMenuFrame(topLeftX: Int, topLeftY: Int, width: Int, height: Int) extends Frame(topLeftX, topLeftY, width, height) {
  var helpers: Helpers = _

  override def render(): Unit = {
    clear(Color.black)
    val y = -10 - Constants.mainMenuItems.length
    Constants.mainMenuItems.zipWithIndex.foreach {
      case ((str, _), id) => write(str, 10, y + id,
        if (id == helpers.state.current.asInstanceOf[MainMenuState].selectedItem) {
          AsciiPanel.yellow
        } else {
          AsciiPanel.white
        },
        AsciiPanel.black
      )
    }
  }
}
