package net.abesto.labyrinth.render.asciipanel

import java.awt.Color

import net.abesto.labyrinth.fsm.InStates
import net.abesto.labyrinth.fsm.States.GamePopupState
import net.abesto.labyrinth.fsm.Transitions.ShowPopupEvent
import net.mostlyoriginal.api.event.common.Subscribe
import squidpony.squidmath.Coord

@InStates(Array(classOf[GamePopupState]))
class Popup extends Frame(0, 0, 0, 0) {
  var title: String = ""
  var text: String = ""

  @Subscribe
  def show(e: ShowPopupEvent): Unit = {
    title = e.title
    text = e.text
  }

  def render(): Unit = {
    val titleWidth = title.length + 4
    // at least two spaces per side
    val lines = text.replace("\n", "\n\n").split('\n')
    val textWidth = lines.maxBy(_.length).length
    val boxWidth = math.max(titleWidth, textWidth) + 4
    // 2 empty columns around the text, plus the box
    val boxHeight = lines.length + 4 // 2 empty lines inside box, plus two lines for the box

    val centerX = panel.getWidthInCharacters / 2
    val centerY = panel.getHeightInCharacters / 2
    val left = centerX - boxWidth / 2
    val top = centerY - boxHeight / 2

    topLeft = Coord.get(left, top)
    size = Coord.get(boxWidth, boxHeight)

    // First, the border
    clear(Color.darkGray)
    // Next, the interior should be black as the background for the text
    clear(topLeft.add(1), bottomRight.subtract(1), Color.black)
    // Write the title onto top of the frame, with brighter background than the frame
    write(s"  $title  ",
      (boxWidth - titleWidth) / 2, 0,
      Color.black, Color.gray.brighter
    )
    // Finally, write the text into the frame
    write(lines, 2, 2, Color.white, Color.black)
  }
}

