package net.abesto.labyrinth.tools.mapgen

import java.io.{File, PrintWriter}

import net.abesto.labyrinth.maze.MazeBuilder

object Main {
  def output(filename: String, mb: MazeBuilder): Unit = {
    val pw = new PrintWriter(new File("game/src/main/resources/maps/" + filename))
    pw.write(
      mb.get.chars.transpose.map(_.mkString).mkString("\n")
    )
    pw.close()
  }

  def main(args: Array[String]): Unit = output(args(0), args(1) match {
    case "gen" =>
      MazeBuilder.random()
    case "hashesToLines" =>
      MazeBuilder.fromFile(args(0)).hashesToLines()
    case "linesToHashes" =>
      MazeBuilder.fromFile(args(0)).linesToHashes()
  })
}
