package net.abesto.labyrinth.render.asciipanel

import java.awt.Color

import net.abesto.labyrinth.components.PopupComponent
import net.abesto.labyrinth.fsm.InStates
import net.abesto.labyrinth.fsm.States.GamePopupState
import net.abesto.labyrinth.fsm.Transitions.ShowPopupEvent
import net.mostlyoriginal.api.event.common.Subscribe
import squidpony.squidmath.Coord

@InStates(Array(classOf[GamePopupState]))
class PopupFrame extends Frame(0, 0, 0, 0) {
  private var _popup: PopupComponent = _
  def popup: PopupComponent = _popup

  @Subscribe
  def show(e: ShowPopupEvent): Unit = {
    _popup = e.popup
  }

  val titlePaddingSize: Int = 2
  val titlePadding: String = " " * titlePaddingSize
  def title: String = s"$titlePadding${popup.title}$titlePadding"
  def titleWidth: Int = title.length
  def titleLeft: Int = (boxWidth - titleWidth) / 2

  // at least two spaces per side
  def lines: Array[String] = popup.text.replace("\n", "\n\n").split('\n')
  def textWidth: Int = lines.maxBy(_.length).length
  def boxWidth: Int = math.max(titleWidth, textWidth) + 4

  // 2 empty columns around the text, plus the box
  def boxHeight: Int = lines.length + 4 // 2 empty lines inside box, plus two lines for the box

  def render(): Unit = {
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
    write(s"  ${popup.title}  ",
      (boxWidth - titleWidth) / 2, 0,
      Color.black, Color.gray.brighter
    )
    // Finally, write the text into the frame
    write(lines, 2, 2, Color.white, Color.black)
    panel.getCursorX
  }
}

