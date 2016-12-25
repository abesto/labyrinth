resolvers += Resolver.sonatypeRepo("releases")

libraryDependencies ++= Seq(
  "net.onedaybeard.artemis" % "artemis-odb" % "2.1.0",
  "net.mostlyoriginal.artemis-odb" % "contrib-eventbus" % "1.2.1",
  "com.squidpony" % "squidlib-util" % "3.0.0-b6",

  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)

// By default, sbt buffers log output for each suite until all tests for that suite complete and causing "spurty"
// output. We recommend you disable sbt's log buffering so you can enjoy ScalaTest's built-in event buffering algorithm,
// which shows the events of one suite as they occur until that suite either completes or a timeout occurs, at which
// point ScalaTest switches a different suite's events.
logBuffered in Test := false
