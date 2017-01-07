package net.abesto.labyrinth.render.asciipanel

import java.awt.Color

import com.artemis.ComponentMapper
import com.artemis.managers.TagManager
import net.abesto.labyrinth.Constants
import net.abesto.labyrinth.components.PromptComponent
import net.abesto.labyrinth.fsm.InStates
import net.abesto.labyrinth.fsm.States.GameSpellInputState

@InStates(Array(classOf[GameSpellInputState]))
class SpellInputFrame(topLeftX: Int, topLeftY: Int, width: Int, height: Int) extends Frame(topLeftX, topLeftY, width, height) {
  var tagManger: TagManager = _
  var spellInputMapper: ComponentMapper[PromptComponent] = _

  def render(): Unit = {
    val input: PromptComponent = spellInputMapper.get(tagManger.getEntityId(Constants.Tags.prompt))
    clear(Color.black)
    write(s"${input.prompt}: ${input.input}", 0, 0, Color.white, Color.black)
    val charUnderCursor = if (input.cursorPosition >= input.input.length) ' ' else input.input(input.cursorPosition)
    write(charUnderCursor, input.prompt.length + 2 + input.cursorPosition, 0, Color.white, Color.darkGray)
  }
}
