package net.abesto.labyrinth.render.asciipanel

import java.awt.Color

import net.abesto.labyrinth.components.PromptComponent
import net.abesto.labyrinth.fsm.InStates
import net.abesto.labyrinth.fsm.States.{PromptAsStatusState, PromptState}
import net.abesto.labyrinth.systems.Helpers

@InStates(Array(classOf[PromptState], classOf[PromptAsStatusState]))
class PromptFrame(topLeftX: Int, topLeftY: Int, width: Int, height: Int) extends Frame(topLeftX, topLeftY, width, height) {
  var helpers: Helpers = _

  def render(): Unit = {
    val prompt: PromptComponent = helpers.prompt
    clear(Color.black)
    write(s"${prompt.prompt}${prompt.input}", 0, 0, prompt.fgColor, prompt.bgColor)
    if (helpers.state.isActive[PromptState]) {
      val charUnderCursor = if (prompt.cursorPosition >= prompt.input.length) ' ' else prompt.input(prompt.cursorPosition)
      write(charUnderCursor, prompt.prompt.length + prompt.cursorPosition, 0, Color.white, Color.darkGray)
    }
  }
}
