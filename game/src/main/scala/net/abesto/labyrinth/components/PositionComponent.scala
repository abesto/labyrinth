package net.abesto.labyrinth.components

import com.badlogic.ashley.core.Component
import squidpony.squidmath.Coord

class PositionComponent(x: Int, y: Int) extends Coord(x, y) with Component {
}

object PositionComponent {
  def apply(x: Int, y: Int): PositionComponent = new PositionComponent(x, y)
  def apply(c: Coord): PositionComponent = new PositionComponent(c.x, c.y)
}
