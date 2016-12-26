resolvers += Resolver.sonatypeRepo("releases")

libraryDependencies ++= Seq(
  "net.onedaybeard.artemis" % "artemis-odb" % "2.1.0",
  "net.onedaybeard.artemis" % "artemis-odb-serializer-json" % "2.1.0",
  "net.mostlyoriginal.artemis-odb" % "contrib-eventbus" % "1.2.1",
  "com.squidpony" % "squidlib-util" % "3.0.0-b6",
  "com.beachape" %% "enumeratum" % "1.5.4",
  "org.reflections" % "reflections" % "0.9.10",
  "org.scala-lang" % "scala-reflect" % scalaVersion.value
)

// By default, sbt buffers log output for each suite until all tests for that suite complete and causing "spurty"
// output. We recommend you disable sbt's log buffering so you can enjoy ScalaTest's built-in event buffering algorithm,
// which shows the events of one suite as they occur until that suite either completes or a timeout occurs, at which
// point ScalaTest switches a different suite's events.
logBuffered in Test := false

// Macro paradise \o/
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
