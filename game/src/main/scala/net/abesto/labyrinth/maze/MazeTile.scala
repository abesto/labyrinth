package net.abesto.labyrinth.maze

import java.awt.Color

import squidpony.squidmath.Coord

abstract class MazeTile(val x: Int, val y: Int, c: Char) {
  val coord: Coord = Coord.get(x, y)
  var char: CharacterData = c

  var visibility: Double = 1
  var seen: Boolean = false

  val blocksSight: Boolean
  val canBeStoodOn: Boolean

  def withChar(c: Char): MazeTile = {
    char.character = c
    this
  }

  def foregroundColorWithShadow: Color = {
    val Array(r, g, b) = char.foregroundColor.getRGBColorComponents(null).map(_ * visibility).map(_.toFloat)
    new Color(r, g, b)
  }
}
