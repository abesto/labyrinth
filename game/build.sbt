resolvers += Resolver.sonatypeRepo("releases")

libraryDependencies ++= Seq(
  "net.onedaybeard.artemis" % "artemis-odb" % "2.1.0",  // Entity-Component-System framework
  "net.onedaybeard.artemis" % "artemis-odb-serializer-json" % "2.1.0",  // Serialization; used for storing items on maps; will provide game saving
  "net.mostlyoriginal.artemis-odb" % "contrib-eventbus" % "1.2.1",  // Who doesn't love events?
  "com.squidpony" % "squidlib-util" % "3.0.0-b6",  // Dungeon generation, shadowcasting, etc. Awesome lib for roguelikes
  "com.beachape" %% "enumeratum" % "1.5.4",  // Enums that happen to play well with artemis-odb-serializer (after some coercing)
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4",  // For parsing spells
  "org.reflections" % "reflections" % "0.9.10",  // Magic
  "org.scala-lang" % "scala-reflect" % scalaVersion.value  // More magic
)

// Macro paradise \o/  a.k.a. Even more magic
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
