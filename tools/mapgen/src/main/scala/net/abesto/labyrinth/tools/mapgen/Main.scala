package net.abesto.labyrinth.tools.mapgen

import net.abesto.labyrinth.maze.MazeBuilder

object Main {
  def main(args: Array[String]): Unit = {
    println(
      new MazeBuilder().generateMaze().get.transpose.map(_.map(_.char).mkString).mkString("\n")
    )
  }
}
