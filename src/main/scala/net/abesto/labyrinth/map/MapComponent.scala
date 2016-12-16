package net.abesto.labyrinth.map

import com.badlogic.ashley.core.Component
import net.abesto.labyrinth.components.PositionComponent

case class MapComponent(tiles: IndexedSeq[IndexedSeq[MapTile]] = IndexedSeq(IndexedSeq())) extends Component {
  def height = tiles.size
  def width = tiles(0).size

  def tile(p: PositionComponent) = tiles(p.y)(p.x)
}
