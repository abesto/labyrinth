package net.abesto.labyrinth.map

import com.badlogic.ashley.core.Component
import net.abesto.labyrinth.components.PositionComponent

case class MapComponent(tiles: Array[Array[MapTile]] = Array(Array())) extends Component {
  def width = tiles.length
  def height = tiles(0).length

  def tile(p: PositionComponent) = tiles(p.x)(p.y)
  def tile(x: Int, y: Int) = tiles(x)(y)
}
