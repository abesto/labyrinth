package net.abesto.labyrinth.maze

import java.awt.Color

class ShallowWaterTile(x: Int, y: Int, c: Char) extends MazeTile(x, y) {
  override val blocksSight: Boolean = false
  override val canBeStoodOn: Boolean = true
  override var char: CharacterData = c
  char.foregroundColor = Color.blue
}
