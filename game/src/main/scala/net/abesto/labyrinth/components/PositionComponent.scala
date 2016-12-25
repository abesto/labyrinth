package net.abesto.labyrinth.components

import com.artemis.Component
import squidpony.squidmath.Coord

class PositionComponent() extends Component {
  var x: Int = _
  var y: Int = _

  def this(coord: Coord) {
    this()
    this.x = coord.getX
    this.y = coord.getY
  }

  def this(x: Int, y: Int) {
    this()
    this.x = x
    this.y = y
  }

  def coord: Coord = Coord.get(x, y)
  def coord_=(coord: Coord): Unit = {
    x = coord.getX
    y = coord.getY
  }

  def +(other: PositionComponent): PositionComponent = new PositionComponent(coord.add(other.coord))
}

