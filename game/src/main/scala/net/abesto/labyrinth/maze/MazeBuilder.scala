package net.abesto.labyrinth.maze

import net.abesto.labyrinth.{Constants, Tiles}
import squidpony.squidgrid.mapping.{DungeonGenerator, DungeonUtility}

import scala.io.Source

class MazeBuilder(protected var maze: Maze) {
  def mapMaze(f: (MazeTile) => MazeTile): MazeBuilder = {
    maze.transform(f)
    this
  }

  def roughFloor(): MazeBuilder = mapMaze {
    case t: FloorTile => new FloorTile(t.x, t.y, maze.tileset.toChar(Tiles.Kind.RoughFloor))
    case t => t
  }

  def smoothFloor(): MazeBuilder = {
    val c = maze.tileset.toChar(Tiles.Kind.SmoothFloor)
    mapMaze {
      case t: FloorTile => new FloorTile(t.x, t.y, c)
      case t => t
    }
  }

  def hashesToLines(): MazeBuilder = {
    val originalTileset = maze.tileset
    maze.translate(Tiles.squidlib)  // So that squidlib can work with it
    val newChars = DungeonUtility.hashesToLines(maze.chars).map(
      _.map {
        case ' ' => '#'  // hashesToLines marks spaces between walls with space; we want #
        case c => c
      }
    )
    mapMaze(t => t.withChar(newChars(t.x)(t.y)))
    maze.translate(originalTileset)  // Translate back to original tileset
    this
  }

  def linesToHashes(): MazeBuilder = {
    val originalTileset = maze.tileset
    maze.translate(Tiles.squidlib)  // So that squidlib can work with it
    val newChars = DungeonUtility.linesToHashes(maze.chars)
    mapMaze(t => t.withChar(newChars(t.x)(t.y)))
    maze.translate(originalTileset)  // Translate back to original tileset
    this
  }

  def get: Maze = maze
}

object MazeBuilder {

  def random(): MazeBuilder = {
    val tileset = Tiles.squidlib // Because we're using the output of squidlib
    val maze = Maze.empty(tileset)
    new DungeonGenerator(Constants.mazeWidth, Constants.mazeHeight).generate().zipWithIndex.foreach {
      case (column, x) => column.zipWithIndex.foreach {
        case (c, y) => maze.update(x, y, c)
      }
    }
    new MazeBuilder(maze)
  }

  def fromFile(name: String, tileCallback: (Tiles.Kind, Int, Int) => Unit = (_, _, _) => Unit): MazeBuilder = {
    val tileset = Tiles.squidlib // Because maps are stored in unicode - easier to work with
    val maze = Maze.empty(tileset)
    Source.fromURL(getClass.getResource(s"/maps/$name")).getLines().zipWithIndex.foreach {
      case (line, y) => line.zipWithIndex.foreach {
        case (char, x) =>
          tileCallback(tileset.toKind(char), x, y)
          maze.update(x, y, char)
      }
    }
    new MazeBuilder(maze)
  }
}
