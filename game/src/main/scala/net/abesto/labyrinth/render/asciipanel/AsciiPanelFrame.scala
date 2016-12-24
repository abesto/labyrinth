package net.abesto.labyrinth.render.asciipanel

import java.awt.Color

import asciiPanel.AsciiPanel
import squidpony.squidmath.Coord

case class AsciiPanelFrame(panel: AsciiPanel, topLeft: Coord, size: Coord) {
  protected val bottomRight: Coord = topLeft.add(size)

  def write(c: Char, x: Int, y: Int, fg: Color, bg: Color): Unit = {
    val pos = topLeft.add(Coord.get(x, y))
    assert(pos.isWithinRectangle(
      topLeft.getX, topLeft.getY,
      bottomRight.getX, bottomRight.getY))
    panel.write(c, pos.x, pos.y, fg, bg)
  }
}

