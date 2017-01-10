package net.abesto.labyrinth.render.asciipanel

import java.awt.Color

import asciiPanel.AsciiPanel
import com.artemis.World
import squidpony.squidmath.Coord

abstract class Frame(initialTopLeftX: Int, initialTopLeftY: Int, initialWidth: Int, initialHeight: Int) {
  var world: World = _
  var panel: AsciiPanel = _

  var topLeft: Coord = Coord.get(initialTopLeftX, initialTopLeftY)
  var size: Coord = Coord.get(initialWidth, initialHeight)
  def bottomRight: Coord = topLeft.add(size)

  def write(c: Char, x: Int, y: Int, fg: Color, bg: Color): Unit = {
    val pos = topLeft.add(Coord.get(
      if (x < 0) size.x + x else x,
      if (y < 0) size.y + y else y))
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
    if (size.x > 0 && size.y > 0) {
      panel.clear(' ', tl.x, tl.y, br.x - tl.x, br.y - tl.y, color, color)
    }
  }

  def clear(color: Color): Unit = {
    clear(topLeft, bottomRight, color)
  }

  def render(): Unit
}

