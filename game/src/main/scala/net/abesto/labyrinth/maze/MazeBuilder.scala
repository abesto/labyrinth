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

  import Tiles.Kind._

  def charToMazeTile(tileset: Tiles.Tileset, c: Char, x: Int, y: Int): MazeTile = {
    tileset.toKind(c) match {
      case SmoothFloor => new FloorTile(x, y, c)
      case Player => new FloorTile(x, y, tileset.toChar(SmoothFloor))
      case WallHash => new WallTile(x, y, c)
      case StairsDown => new StaircaseDownTile(x, y, c)
      case Water => new WaterTile(x, y, c)
      case ShallowWater => new ShallowWaterTile(x, y, c)
    }
  }

  def random(): MazeBuilder = {
    val tileset = Tiles.squidlib // Because we're using the output of squidlib
    new MazeBuilder(Maze(tileset,
      new DungeonGenerator(Constants.width, Constants.height).generate().zipWithIndex.map {
        case (column, x) => column.zipWithIndex.map {
          case (c, y) => charToMazeTile(tileset, c, x, y)
        }
      }
    ))
  }

  def fromFile(name: String, tileCallback: (Tiles.Kind.Value, Int, Int) => Unit = (_, _, _) => Unit): MazeBuilder = {
    val tileset = Tiles.squidlib // Because maps are stored in unicode - easier to work with
    new MazeBuilder(Maze(tileset,
      Source.fromURL(getClass.getResource(s"/maps/$name")).getLines().zipWithIndex.map {
        case (line, y) => line.zipWithIndex.map {
          case (char, x) =>
            tileCallback(tileset.toKind(char), x, y)
            charToMazeTile(tileset, char, x, y)
        }.toArray
      }.toArray.transpose
    ))
  }
}
