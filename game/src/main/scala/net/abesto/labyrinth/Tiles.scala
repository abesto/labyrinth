package net.abesto.labyrinth

import enumeratum._

import scala.collection.immutable.IndexedSeq
import scala.language.implicitConversions
import scala.util.Random

object Tiles {
  sealed abstract class Kind extends ArtemisJsonEnumEntry(Kind)

  object Kind extends Enum[Kind] {
    val values: IndexedSeq[Kind] = findValues

    // Walls
    case object WallCornerNorthWest extends Kind
    case object WallCornerNorthEast extends Kind
    case object WallCornerSouthWest extends Kind
    case object WallCornerSouthEast extends Kind

    case object WallEastWest extends Kind
    case object WallNorthSouth extends Kind

    case object WallEastWestTNorth extends Kind
    case object WallEastWestTSouth extends Kind

    case object WallNorthSouthTWest extends Kind
    case object WallNorthSouthTEast extends Kind

    case object WallCross extends Kind

    // Other dungeon features
    case object SmoothFloor extends Kind
    case object RoughFloor extends Kind

    case object StairsDown extends Kind
    case object StairsUp extends Kind

    case object WallHash extends Kind
    case object Water extends Kind
    case object ShallowWater extends Kind

    case object Book extends Kind

    // Characters
    case object Player extends Kind

    // Interface elements
    case object UpArrow extends Kind
    case object DownArrow extends Kind
    case object LeftArrow extends Kind
    case object RightArrow extends Kind

    // Alphanumeric
    case class AlphaNum(char: Char) extends Kind {
      assert(char.isLetterOrDigit || ": @".contains(char))
    }
  }

  import Kind._

  class Tileset(spec: (Kind, Seq[Char])*) {
    protected val kindToChars: Map[Kind, Seq[Char]] = Map(spec:_*)
    protected val charToKind: Map[Char, Kind] = kindToChars.flatMap(p => p._2.map((_, p._1)))

    def toChar(kind: Kind): Char = kind match {
      case AlphaNum(c) => c
      case _ =>
        val chars = kindToChars(kind)
        if (chars.length == 1) {
          chars.head
        } else {
          chars(Random.nextInt(chars.length))
        }
    }

    def toKind(char: Char): Kind = charToKind.getOrElse(char, AlphaNum(char))

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
    Player -> 1,

    // Interface elements
    UpArrow -> 24, DownArrow -> 25, RightArrow -> 26, LeftArrow -> 27
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
    Player -> '☺',

    // Interface elements
    UpArrow -> '↑', DownArrow -> '↓', RightArrow -> '→', LeftArrow -> '←'
  )
}

