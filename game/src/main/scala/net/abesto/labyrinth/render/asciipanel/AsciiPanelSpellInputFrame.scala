package net.abesto.labyrinth.render.asciipanel

import java.awt.Color

import asciiPanel.AsciiPanel
import com.artemis.ComponentMapper
import com.artemis.managers.TagManager
import net.abesto.labyrinth.Constants
import net.abesto.labyrinth.components.SpellInputComponent
import squidpony.squidmath.Coord

class AsciiPanelSpellInputFrame(panel: AsciiPanel, topLeft: Coord, size: Coord) extends AsciiPanelFrame(panel, topLeft, size) {
  var tagManger: TagManager = _
  var spellInputMapper: ComponentMapper[SpellInputComponent] = _

  def render(): Unit = {
    val input: SpellInputComponent = spellInputMapper.get(tagManger.getEntityId(Constants.Tags.spellInput))
    clear(Color.black)
    if (input.isActive) {
      write(s"${input.prompt}: ${input.input}", 0, 0, Color.white, Color.black)
      val charUnderCursor = if (input.cursorPosition >= input.input.length) ' ' else input.input(input.cursorPosition)
      write(charUnderCursor, input.prompt.length + 2 + input.cursorPosition, 0, Color.white, Color.darkGray)
    }
  }
}
