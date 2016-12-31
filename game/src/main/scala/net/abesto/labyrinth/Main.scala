package net.abesto.labyrinth

import net.abesto.labyrinth.render.asciipanel.Renderer
import net.abesto.labyrinth.ui.ApplicationMainFrame

object Main {
  def main(args: Array[String]): Unit = {
    val renderer = new Renderer()
    val world = World.world(renderer)

    val app = new ApplicationMainFrame(world, renderer)
    app.setup()

    world.setDelta(1)
    world.process()
  }
}
