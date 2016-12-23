package net.abesto.labyrinth.maze

import com.badlogic.ashley.core.Component
import net.abesto.labyrinth.components.PositionComponent

case class MazeComponent(tiles: Array[Array[MazeTile]] = Array(Array())) extends Component {
  def width: Int = tiles.length
  def height: Int = tiles(0).length

  def tile(p: PositionComponent): MazeTile = tiles(p.x)(p.y)
  def tile(x: Int, y: Int): MazeTile = tiles(x)(y)

  def chars: Array[Array[Char]] = tiles.map(_.map(_.char.character))
}
