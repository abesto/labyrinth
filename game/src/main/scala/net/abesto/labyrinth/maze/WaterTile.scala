package net.abesto.labyrinth.maze

import java.awt.Color

import net.abesto.labyrinth.Tiles

class WaterTile(x: Int, y: Int, c: Char) extends MazeTile(x, y) {
  override val blocksSight: Boolean = false
  override val canBeStoodOn: Boolean = false
  override var char: CharacterData = c
  char.foregroundColor = Color.blue
}
