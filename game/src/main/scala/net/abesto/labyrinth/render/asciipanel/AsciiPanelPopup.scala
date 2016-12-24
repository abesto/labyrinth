package net.abesto.labyrinth.render.asciipanel

import java.awt.Color

import asciiPanel.AsciiPanel
import com.badlogic.ashley.signals.{Listener, Signal}
import net.abesto.labyrinth.signals.{PopupData, Signals}
import squidpony.squidmath.Coord

class AsciiPanelPopup(panel: AsciiPanel) {
  var show: Boolean = false
  var title: String = ""
  var text: String = ""

  Signals.showPopup.add(new Listener[PopupData]{
    override def receive(signal: Signal[PopupData], data: PopupData): Unit = {
      show = true
      title = data.title
      text = data.text
    }
  })

  Signals.hidePopup.add(new Listener[Null] {
    override def receive(signal: Signal[Null], `object`: Null): Unit = { show = false }
  })

  def render(): Unit = {
    if (!show) {
      return
    }
    val titleWidth = title.length + 4  // at least two spaces per side
    val lines = text.split('\n')
    val textWidth = lines.maxBy(_.length).length
    val boxWidth = math.max(titleWidth, textWidth) + 4  // 2 empty columns around the text, plus the box
    val boxHeight = lines.length + 4 // 2 empty lines inside box, plus two lines for the box

    val centerX = panel.getWidthInCharacters / 2
    val centerY = panel.getHeightInCharacters / 2
    val left = centerX - boxWidth / 2
    val top = centerY - boxHeight / 2

    val frame = AsciiPanelFrame(panel, Coord.get(left, top), Coord.get(boxWidth, boxHeight))

    // First, the border
    frame.clear(Color.darkGray)
    // Next, the interior should be black as the background for the text
    frame.clear(frame.topLeft.add(1), frame.bottomRight.subtract(1), Color.black)
    // Write the title onto top of the frame, with brighter background than the frame
    frame.write(s"  $title  ",
      (boxWidth - titleWidth) / 2, 0,
      Color.black, Color.gray.brighter
    )
    // Finally, write the text into the frame
    frame.write(lines, 2, 2, Color.white, Color.black)
  }
}
