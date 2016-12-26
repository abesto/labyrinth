package net.abesto.labyrinth

import net.abesto.labyrinth.events.LoadMazeEvent
import net.abesto.labyrinth.render.asciipanel.AsciiPanelRenderer
import net.mostlyoriginal.api.event.common.EventSystem

object Game {
  def main(args: Array[String]): Unit = {
    val renderer = AsciiPanelRenderer()
    val world = WorldDefinition.buildWorld(renderer)

    val app = new ApplicationMain(world, renderer)
    app.setup()

    // Kick things off
    world.getSystem(classOf[EventSystem]).dispatch(LoadMazeEvent("1-dry"))
    world.setDelta(1)
    world.process()
  }
}
