import sbt._
import Keys._

object LabyrinthBuild extends Build {
  lazy val root = Project(id = "labyrinth",
                          base = file(".")) aggregate(game, mapgen)

  lazy val game = Project(id = "game",
                         base = file("game"))

  lazy val mapgen = Project(id = "mapgen",
                         base = file("tools/mapgen")) dependsOn(game)
}
