import sbt._
import Keys._

object LabyrinthBuild extends Build {
  lazy val root: Project = Project(id = "labyrinth",
                          base = file(".")) aggregate(game, mapgen)

  lazy val game: Project = Project(id = "game",
                         base = file("game")) dependsOn macros

  lazy val mapgen: Project = Project(id = "mapgen",
                         base = file("tools/mapgen")) dependsOn game

  lazy val macros: Project = Project(id = "macros", base = file("macros"))
}
