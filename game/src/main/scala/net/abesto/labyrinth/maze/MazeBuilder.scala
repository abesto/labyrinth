package net.abesto.labyrinth.maze

import net.abesto.labyrinth.{Constants, Tiles}
import squidpony.squidgrid.mapping.{DungeonGenerator, DungeonUtility}

import scala.io.Source
import scala.util.Random

class MazeBuilder {
  type TileMap = Array[Array[MazeTile]]

  protected var maze: TileMap = _

  // squidlib uses unicode, but we use asciipanel + dwarfortress tiles. translate.
  protected val unicodeToAsciiDrawingCharacters = Map(
    '│' -> Tiles.smoothWallNS,
    '─' -> Tiles.smoothWallEW,

    '└' -> Tiles.smoothWallSW,
    '┌' -> Tiles.smoothWallNW,
    '┘' -> Tiles.smoothWallSE,
    '┐' -> Tiles.smoothWallNE,

    '├' -> Tiles.smoothWallNSE,
    '┤' -> Tiles.smoothWallNSW,
    '┬' -> Tiles.smoothWallEWS,
    '┴' -> Tiles.smoothWallEWN,

    '┼' -> Tiles.smoothWallCross
  )

  //def hashesToLinesAscii(maze: Array[Array[Char]]): Array[Array[MapTile]] = {}

  def mapMaze(f: (MazeTile) => MazeTile): MazeBuilder = {
    maze = maze.map(_.map(f))
    this
  }

  def generateMaze(): MazeBuilder = {
    maze = new DungeonGenerator(Constants.width, Constants.height).generate().zipWithIndex.map {
      case (column, x) => column.zipWithIndex.map {
        case ('.', y) => new FloorTile(x, y)
        case ('#', y) => new WallTile(x, y)
      }
    }
    this
  }

  def charToMapTile(c: Char, x: Int, y: Int): MazeTile = c match {
    case '.' => new FloorTile(x, y)
    case '@' => new FloorTile(x, y)
    case '#' => new WallTile(x, y)
    case '>' => new StaircaseDownTile(x, y)
    case '≈' => new WaterTile(x, y)
  }

  def loadMaze(name: String, tileCallback: (Char, Int, Int) => Unit = (_, _, _) => Unit): MazeBuilder = {
    maze = Source.fromURL(getClass.getResource(s"/maps/$name")).getLines().zipWithIndex.map {
      case (line, y) => line.zipWithIndex.map {
        case (char, x) =>
          tileCallback(char, x, y)
          charToMapTile(char, x, y)
      }.toArray
    }.toArray.transpose
    this
  }

  def roughFloor(): MazeBuilder = mapMaze {
      case t: FloorTile => new FloorTile(t.x, t.y, Tiles.roughFloors(Random.nextInt(Tiles.roughFloors.length)))
      case t => t
    }

  def hashesToLines(): MazeBuilder = {
    val newChars = DungeonUtility.hashesToLines(maze.map(_.map(_.char.character)))
    mapMaze(t => t.withChar(newChars(t.x)(t.y)))
  }

  def unicodeToAscii(): MazeBuilder = mapMaze(
    t => t.withChar(unicodeToAsciiDrawingCharacters.getOrElse(t.char.character, t.char.character))
  )

  def get: TileMap = maze
}
