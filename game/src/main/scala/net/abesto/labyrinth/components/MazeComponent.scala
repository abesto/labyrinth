package net.abesto.labyrinth.components

import com.artemis.Component
import net.abesto.labyrinth.maze.{Maze, MazeTile}

class MazeComponent() extends Component {
  var maze: Maze = _

  def tile(p: PositionComponent): MazeTile = maze.tile(p)
  def tile(x: Int, y: Int): MazeTile = maze.tile(x, y)

}
