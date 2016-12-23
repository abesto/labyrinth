package net.abesto.labyrinth.maze

class StaircaseDownTile(x: Int, y: Int) extends MazeTile(x, y) {
  override val blocksSight: Boolean = false
  override val canBeStoodOn: Boolean = true
  override var char: CharacterData = '>'
}
