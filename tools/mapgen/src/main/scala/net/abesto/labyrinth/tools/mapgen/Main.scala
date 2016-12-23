package net.abesto.labyrinth.tools.mapgen

import net.abesto.labyrinth.maze.MazeBuilder

object Main {
  def main(args: Array[String]): Unit = {
    println(
      MazeBuilder.random().hashesToLines().smoothFloor().get
        .tiles.transpose.map(_.map(_.char).mkString).mkString("\n")
    )
  }
}
