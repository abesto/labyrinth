package net.abesto.labyrinth.maze

import java.awt.Color

class WaterTile(x: Int, y: Int, c: Char) extends MazeTile(x, y, c) {
  override val blocksSight: Boolean = false
  override val canBeStoodOn: Boolean = false
  char.foregroundColor = Color.blue
}
