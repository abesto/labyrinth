package net.abesto.labyrinth.components

import com.badlogic.ashley.core.Component
import squidpony.squidmath.Coord

case class PositionComponent(coord: Coord) extends Component {
  def +(other: PositionComponent): PositionComponent = new PositionComponent(coord.add(other.coord))
  def x: Int = coord.getX
  def y: Int = coord.getY
}

object PositionComponent {
  def apply(x: Int, y: Int): PositionComponent = new PositionComponent(Coord.get(x, y))
}
