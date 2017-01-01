package net.abesto.labyrinth.maze

class FloorTile(x: Int, y: Int, c: Char) extends MazeTile(x, y, c) {
  override val blocksSight: Boolean = false
  override val canBeStoodOn: Boolean = true
}
