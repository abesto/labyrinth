resolvers += Resolver.sonatypeRepo("releases")

libraryDependencies ++= Seq(
  "net.onedaybeard.artemis" % "artemis-odb" % "2.1.0",
  "net.mostlyoriginal.artemis-odb" % "contrib-eventbus" % "1.2.1",
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,

  // Logging
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0"
)

// Macro paradise \o/
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

