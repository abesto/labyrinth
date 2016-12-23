package net.abesto.labyrinth.maze

import com.badlogic.ashley.core.Component
import net.abesto.labyrinth.components.PositionComponent

case class MazeComponent(maze: Maze) extends Component {
  def tile(p: PositionComponent): MazeTile = maze.tile(p)
  def tile(x: Int, y: Int): MazeTile = maze.tile(x, y)

}
