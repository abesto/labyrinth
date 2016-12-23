package net.abesto.labyrinth.maze

class StaircaseDownTile(x: Int, y: Int, c: Char) extends MazeTile(x, y) {
  override val blocksSight: Boolean = false
  override val canBeStoodOn: Boolean = true
  override var char: CharacterData = c
}
