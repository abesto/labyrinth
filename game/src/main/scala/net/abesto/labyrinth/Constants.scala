package net.abesto.labyrinth

object Constants {
  val mazeWidth = 70
  val mazeHeight = 40

  val messageAreaHeight = 5

  val fullWidth: Int = mazeWidth
  val fullHeight: Int = mazeHeight + messageAreaHeight

  val sightRadius = 10

  object Tags {
    val maze = "MAZE"
    val player = "PLAYER"
  }
}
