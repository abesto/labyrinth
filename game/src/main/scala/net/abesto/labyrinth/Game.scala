package net.abesto.labyrinth

import net.abesto.labyrinth.render.asciipanel.AsciiPanelRenderer
import net.abesto.labyrinth.systems.MazeLoaderSystem

object Game {
  def main(args: Array[String]): Unit = {
    val renderer = AsciiPanelRenderer()
    val world = WorldDefinition.buildWorld(renderer)

    val app = new ApplicationMain(world, renderer)
    app.setup()

    // Kick things off
    world.getSystem(classOf[MazeLoaderSystem]).load("1-dry")
    world.setDelta(1)
    world.process()
  }
}
