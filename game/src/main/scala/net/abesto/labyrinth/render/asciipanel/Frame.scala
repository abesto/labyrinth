package net.abesto.labyrinth.render.asciipanel

import java.awt.Color

import asciiPanel.AsciiPanel
import squidpony.squidmath.Coord

case class Frame(panel: AsciiPanel, topLeft: Coord, size: Coord) {
  val bottomRight: Coord = topLeft.add(size)

  def write(c: Char, x: Int, y: Int, fg: Color, bg: Color): Unit = {
    val pos = topLeft.add(Coord.get(x, y))
    assert(pos.isWithinRectangle(
      topLeft.getX, topLeft.getY,
      bottomRight.getX, bottomRight.getY))
    panel.write(c, pos.x, pos.y, fg, bg)
  }

  def write(line: String, x: Int, y: Int, fg: Color, bg: Color): Unit =
    line.zipWithIndex.foreach {
      case (c, cx) => write(c, x + cx, y, fg, bg)
    }

  def write(lines: Iterable[String], x: Int, y: Int, fg: Color, bg: Color): Unit =
    lines.zipWithIndex.foreach {
      case (line, lineY) => write(line, x, y + lineY, fg, bg)
    }

  def clear(tl: Coord, br: Coord, color: Color): Unit = {
    panel.clear(' ', tl.x, tl.y, br.x - tl.x, br.y - tl.y, color, color)
  }

  def clear(color: Color): Unit = {
    clear(topLeft, bottomRight, color)
  }
}

