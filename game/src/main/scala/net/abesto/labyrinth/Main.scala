package net.abesto.labyrinth

import net.abesto.labyrinth.events.{ActivateInputMapEvent, LoadMazeEvent}
import net.abesto.labyrinth.render.asciipanel.GameRenderer
import net.abesto.labyrinth.ui.{ApplicationMainFrame, InputMap}
import net.mostlyoriginal.api.event.common.EventSystem

object Main {
  def main(args: Array[String]): Unit = {
    game()
  }

  def game(): Unit = {
    val renderer = new GameRenderer()
    val world = GameWorld.world(renderer)

    val app = new ApplicationMainFrame(world, renderer)
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
