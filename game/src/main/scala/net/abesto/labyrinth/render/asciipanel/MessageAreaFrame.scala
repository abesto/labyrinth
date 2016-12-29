package net.abesto.labyrinth.render.asciipanel

import asciiPanel.AsciiPanel
import net.abesto.labyrinth.events.MessageEvent
import net.mostlyoriginal.api.event.common.Subscribe
import squidpony.squidmath.Coord

class MessageAreaFrame(panel: AsciiPanel, topLeft: Coord, size: Coord)
  extends Frame(panel, topLeft, size) {
  protected var lastMessageCount: Int = 0
  protected var messages: Seq[String] = Seq.empty
  protected var lines: Seq[String] = Seq.empty

  // Later on, this should fetch messages from a centralized MessageHistory
  // Especially once repeat message rollups are implemented, to make the display here and in message history view
  // consistent
  @Subscribe
  def push(message: MessageEvent): Unit = {
    messages :+= message.message
    updateLines()
  }

  def updateLines(): Unit = {
    lines = messages.flatMap(breakIntoLines).takeRight(size.y)
  }

  def breakIntoLines(s: String): Seq[String] =
    if (s.length > size.x) {
      val lastSpaceBeforeEOL = size.x - s.take(size.x).reverse.indexOf(' ')
      val (line, rest) = s.splitAt(lastSpaceBeforeEOL)
      line +: breakIntoLines(rest)
    } else {
      Seq(s)
    }

  def render(): Unit = {
    panel.clear(' ', topLeft.x, topLeft.y, bottomRight.x, bottomRight.y, AsciiPanel.white, AsciiPanel.black)
    write(lines, 0, 0, AsciiPanel.white, AsciiPanel.black)
  }
}

