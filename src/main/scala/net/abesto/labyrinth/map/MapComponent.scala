package net.abesto.labyrinth.map

import com.badlogic.ashley.core.Component
import net.abesto.labyrinth.components.PositionComponent

case class MapComponent(tiles: Array[Array[MapTile]] = Array(Array())) extends Component {
  def height = tiles.length
  def width = tiles(0).length

  def tile(p: PositionComponent) = tiles(p.y)(p.x)
  def tile(x: Int, y: Int) = tiles(y)(x)
}
