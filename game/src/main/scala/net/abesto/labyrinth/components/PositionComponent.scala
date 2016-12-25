package net.abesto.labyrinth.components

import com.artemis.Component
import squidpony.squidmath.Coord

class PositionComponent() extends Component {
  var coord: Coord = _

  def this(coord: Coord) {
    this()
    this.coord = coord
  }

  def this(x: Int, y: Int) {
    this(Coord.get(x, y))
  }

  def +(other: PositionComponent): PositionComponent = new PositionComponent(coord.add(other.coord))
  def x: Int = coord.getX
  def y: Int = coord.getY
}

