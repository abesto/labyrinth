package net.abesto.labyrinth.render.asciipanel

import asciiPanel.AsciiPanel
import com.badlogic.ashley.signals.{Listener, Signal}
import net.abesto.labyrinth.signals.Signals
import squidpony.squidmath.Coord

class AsciiPanelMessageAreaFrame(panel: AsciiPanel, topLeft: Coord, size: Coord)
  extends AsciiPanelFrame(panel, topLeft, size)
{
  protected var lastMessageCount: Int = 0
  protected var messages: Seq[String] = Seq.empty
  protected var lines: Seq[String] = Seq.empty

  def push(message: String): Unit = {
    messages :+= message
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

  // Later on, this should fetch messages from a centralized MessageHistory
  // Especially once repeat message rollups are implemented, to make the display here and in message history view
  // consistent
  Signals.message.add(new Listener[String] {
    override def receive(signal: Signal[String], msg: String): Unit = push(msg)
  })
}
