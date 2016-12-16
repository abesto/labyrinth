package net.abesto.labyrinth.map

abstract case class MapTile(x: Int, y: Int) {
  var visibility: Double = 1

  val blocksSight: Boolean
  val canBeStoodOn: Boolean
  def char: Char
}
