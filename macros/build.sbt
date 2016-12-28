resolvers += Resolver.sonatypeRepo("releases")

libraryDependencies ++= Seq(
  "net.onedaybeard.artemis" % "artemis-odb" % "2.1.0",
  "net.mostlyoriginal.artemis-odb" % "contrib-eventbus" % "1.2.1",
  "org.scala-lang" % "scala-reflect" % scalaVersion.value
)

// Macro paradise \o/
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

