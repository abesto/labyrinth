package net.abesto.labyrinth.maze

import java.awt.Color

class ShallowWaterTile(x: Int, y: Int, c: Char) extends MazeTile(x, y, c) {
  override val blocksSight: Boolean = false
  override val canBeStoodOn: Boolean = true
  char.foregroundColor = Color.blue
}
