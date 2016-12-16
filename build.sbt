name := "labyrinth"

version := "1.0"

scalaVersion := "2.12.1"

resolvers += Resolver.sonatypeRepo("releases")

libraryDependencies ++= Seq(
  "com.badlogicgames.ashley" % "ashley" % "1.7.0"
)
