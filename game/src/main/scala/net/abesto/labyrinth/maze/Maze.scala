package net.abesto.labyrinth.maze

import net.abesto.labyrinth.Tiles
import net.abesto.labyrinth.components.PositionComponent
import squidpony.squidmath.Coord

case class Maze(var tileset: Tiles.Tileset, var tiles: Array[Array[MazeTile]]) {
  def tile(p: PositionComponent): MazeTile = tile(p.coord)
  def tile(c: Coord): MazeTile = tiles(c.x)(c.y)
  def tile(x: Int, y: Int): MazeTile = tiles(x)(y)

  def update(t: MazeTile): Unit = {
    tiles(t.x)(t.y)  = t
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
}
