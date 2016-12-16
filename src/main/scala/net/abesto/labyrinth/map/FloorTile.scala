package net.abesto.labyrinth.map

class FloorTile(x: Int, y: Int) extends MapTile(x, y) {
  override val blocksSight: Boolean = false
  override val canBeStoodOn: Boolean = true
}
