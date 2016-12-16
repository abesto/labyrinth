package net.abesto.labyrinth.map

class WallTile(x: Int, y: Int) extends MapTile(x, y) {
  override val blocksSight: Boolean = true
  override val canBeStoodOn: Boolean = false
}
