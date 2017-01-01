package net.abesto.labyrinth.maze

class WallTile(x: Int, y: Int, c: Char) extends MazeTile(x, y, c) {
  override val blocksSight: Boolean = true
  override val canBeStoodOn: Boolean = false
}
