package net.abesto.labyrinth.map

class WallTile(x: Int, y: Int, c: Char = '#') extends MapTile(x, y) {
  override val blocksSight: Boolean = true
  override val canBeStoodOn: Boolean = false
  override val char: Char = c
}
