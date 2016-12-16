package net.abesto.labyrinth.map

class FloorTile(x: Int, y: Int, c: Char = '.') extends MapTile(x, y) {
  override val blocksSight: Boolean = false
  override val canBeStoodOn: Boolean = true
  override val char: Char = c
}
