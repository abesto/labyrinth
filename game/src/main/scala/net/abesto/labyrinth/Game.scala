package net.abesto.labyrinth

import com.badlogic.ashley.core.Engine
import net.abesto.labyrinth.render.asciipanel.AsciiPanelRenderer

object Game {
  def main(args: Array[String]): Unit = {
    val engine = new Engine
    val world = new World(engine)

    val renderer = AsciiPanelRenderer()
    engine.addSystem(renderer)

    val app = new ApplicationMain(engine, renderer)
    app.setup()

    // Kick things off
    EngineAccessors.loadMap(engine, "1-dry")
    engine.update(1)
  }
}
