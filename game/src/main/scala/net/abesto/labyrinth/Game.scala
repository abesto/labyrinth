package net.abesto.labyrinth

import net.abesto.labyrinth.events.{ActivateInputMapEvent, LoadMazeEvent}
import net.abesto.labyrinth.render.asciipanel.Renderer
import net.abesto.labyrinth.ui.{ApplicationMain, InputMap}
import net.mostlyoriginal.api.event.common.EventSystem

object Game {
  def main(args: Array[String]): Unit = {
    val renderer = Renderer()
    val world = WorldDefinition.buildWorld(renderer)

    val app = new ApplicationMain(world, renderer)
    app.setup()

    // Kick things off
    val eventSystem = world.getSystem(classOf[EventSystem])
    eventSystem.registerEvents(app)

    eventSystem.dispatch(LoadMazeEvent("1-dry"))
    eventSystem.dispatch(ActivateInputMapEvent(InputMap.mainInputMap))
    world.setDelta(1)
    world.process()
  }
}
