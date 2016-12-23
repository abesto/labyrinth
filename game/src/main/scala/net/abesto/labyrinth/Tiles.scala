package net.abesto.labyrinth

import scala.util.Random

object Tiles {
  object Kind extends Enumeration {
    val
    // Walls
    WallCornerNorthWest, WallCornerNorthEast, WallCornerSouthWest, WallCornerSouthEast,
    WallEastWest, WallNorthSouth,
    WallEastWestTNorth, WallEastWestTSouth,
    WallNorthSouthTWest, WallNorthSouthTEast,
    WallCross,

    // Other dungeon features
    SmoothFloor, RoughFloor,
    StairsDown, StairsUp,
    WallHash, Water, ShallowWater,
    Book,

    // Characters
    Player

    = Value
  }
  import Kind._

  class Tileset(spec: (Kind.Value, Seq[Char])*) {
    protected val kindToChars: Map[Kind.Value, Seq[Char]] = Map(spec:_*)
    protected val charToKind: Map[Char, Kind.Value] = kindToChars.flatMap(p => p._2.map((_, p._1)))

    def toChar(kind: Kind.Value): Char = {
      val chars = kindToChars(kind)
      if (chars.length == 1) {
        chars.head
      } else {
        chars(Random.nextInt(chars.length))
      }
    }

    def toKind(char: Char): Kind.Value = charToKind(char)

    def translate(char: Char, to: Tileset): Char = {
      val kind = toKind(char)
      val index = kindToChars(kind).indexOf(char)
      val toChars = to.kindToChars(kind)
      toChars(index % toChars.length)
    }
  }

  // Syntax sugar for nicer tileset definitions
  implicit def intToCharSeq(i: Int): Seq[Char] = Seq(i.toChar)
  implicit def charToCharSeq(c: Char): Seq[Char] = Seq(c)

  // http://dwarffortresswiki.org/index.php/DF2014:Tilesets
  // As used by AsciiPanel
  val dwarfFortress: Tileset = new Tileset(
    // Walls
    WallCornerNorthWest -> 201, WallCornerNorthEast -> 187, WallCornerSouthWest -> 200, WallCornerSouthEast -> 188,
    WallEastWest -> 205, WallNorthSouth -> 186,
    WallEastWestTNorth -> 202, WallEastWestTSouth -> 203,
    WallNorthSouthTWest -> 185, WallNorthSouthTEast -> 204,
    WallCross -> 206, WallHash -> '#',

    // Other dungeon features
    SmoothFloor -> '.', RoughFloor -> Seq(39, 44, 46, 96),
    StairsDown -> '>', StairsUp -> '<',
    Water -> 247, ShallowWater -> 126,
    Book -> 8,

    // Characters
    Player -> 1
  )

  // In Unicode
  // As used by squidlib, and stored in map definitions
  val squidlib: Tileset = new Tileset(
    // Walls
    WallCornerNorthWest -> '┌', WallCornerNorthEast -> '┐', WallCornerSouthWest -> '└', WallCornerSouthEast -> '┘',
    WallNorthSouth -> '│', WallEastWest -> '─',
    WallEastWestTNorth -> '┴', WallEastWestTSouth -> '┬',
    WallNorthSouthTWest -> '┤', WallNorthSouthTEast -> '├',
    WallCross -> '┼', WallHash -> '#',

    // Other dungeon features
    SmoothFloor -> '.', RoughFloor -> Seq('\'', ',', '.', '`'),
    StairsDown -> '>', StairsUp -> '<',
    Water -> '≈', ShallowWater -> '~',
    Book -> '◘',

    // Characters
    Player -> '☺'
  )
}

