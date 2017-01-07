package net.abesto.labyrinth.maze

import net.abesto.labyrinth.Tiles.Kind._
import net.abesto.labyrinth.components.PositionComponent
import net.abesto.labyrinth.{Constants, Tiles}
import squidpony.squidmath.Coord

case class Maze(var tileset: Tiles.Tileset, var tiles: Array[Array[MazeTile]]) {
  def tile(p: PositionComponent): MazeTile = tile(p.coord)
  def tile(c: Coord): MazeTile = tiles(c.x)(c.y)
  def tile(x: Int, y: Int): MazeTile = tiles(x)(y)

  def update(x: Int, y: Int, kind: Tiles.Kind, c: Char): Unit = {
    val tile = kind match {
      case SmoothFloor | RoughFloor => new FloorTile(x, y, c)
      case Player => new FloorTile(x, y, tileset.toChar(SmoothFloor))
      case WallCornerNorthWest | WallEastWestTNorth | WallCornerNorthEast |
           WallNorthSouthTWest | WallHash           | WallNorthSouthTEast |
           WallCornerSouthWest | WallEastWestTSouth | WallCornerSouthEast |
           WallNorthSouth | WallEastWest | WallCross => new WallTile(x, y, c)
      case StairsDown => new StaircaseDownTile(x, y, c)
      case Water => new WaterTile(x, y, c)
      case ShallowWater => new ShallowWaterTile(x, y, c)
    }
    tiles(x)(y) = tile
  }
  def update(x: Int, y: Int, kind: Tiles.Kind): Unit = {
    update(x, y, kind, tileset.toChar(kind))
  }
  def update(x: Int, y: Int, c: Char): Unit = {
    update(x, y, tileset.toKind(c), c)
  }

  def chars: Array[Array[Char]] = tiles.map(_.map(_.char.character))

  def transform(f: (MazeTile) => MazeTile): Unit = {
    tiles = tiles.map(_.map(f))
  }

  def translate(to: Tiles.Tileset): Unit = {
    if (to != tileset) {
      val oldTileset = tileset
      tileset = to
      transform(tile => tile.withChar(oldTileset.translate(tile.char.character, tileset)))
    }
  }

  def withTileset[T](ts: Tiles.Tileset, f: (Maze) => T): T = {
    val originalTileset = tileset
    translate(ts)
    val retval = f(this)
    translate(originalTileset)
    retval
  }
}

object Maze {
  def empty(tileset: Tiles.Tileset): Maze = Maze(tileset, Array.fill(Constants.mazeWidth, Constants.mazeHeight)(null))
}
