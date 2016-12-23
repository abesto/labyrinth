package net.abesto.labyrinth.maze

import java.awt.Color

abstract case class MazeTile(x: Int, y: Int) {
  var visibility: Double = 1
  var seen: Boolean = false

  val blocksSight: Boolean
  val canBeStoodOn: Boolean
  var char: CharacterData

  def withChar(c: Char): MazeTile = {
    char.character = c
    this
  }

  def foregroundColorWithShadow: Color = {
    val Array(r, g, b) = char.foregroundColor.getRGBColorComponents(null).map(_ * visibility).map(_.toFloat)
    new Color(r, g, b)
  }
}