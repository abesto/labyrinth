package net.abesto.labyrinth

import org.scalatest._

class TilesSpec extends FlatSpec with Matchers {

  behavior of "Tile families"

  "DwarfFortress tile family" should "define all tiles" in {
    Tiles.Kind.values.foreach(kind =>
      Tiles.dwarfFortress.toChar(kind)
    )
  }

  "Unicode tile family" should "define all tiles" in {
    Tiles.Kind.values.foreach(kind =>
      Tiles.squidlib.toChar(kind)
    )
  }
}
