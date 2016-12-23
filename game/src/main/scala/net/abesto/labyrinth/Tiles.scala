package net.abesto.labyrinth

object Tiles {
  // http://dwarffortresswiki.org/index.php/DF2014:Tilesets
  val civilian = 1.toChar
  val military = 2.toChar
  
  val book = 8.toChar

  val smoothWallNS = 186.toChar
  val smoothWallNE = 187.toChar
  val smoothWallSE = 188.toChar
  val smoothWallSW = 200.toChar
  val smoothWallNW = 201.toChar
  val smoothWallEW = 205.toChar
  val smoothWallNSE = 204.toChar
  val smoothWallNSW = 185.toChar
  val smoothWallEWS = 203.toChar
  val smoothWallEWN = 202.toChar
  val smoothWallCross = 206.toChar

  val water = 247.toChar

  val roughFloors = Seq(39, 44, 46, 96).map(_.toChar)
}
