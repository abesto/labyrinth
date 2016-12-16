package net.abesto.labyrinth

import com.badlogic.ashley.core.Engine
import net.abesto.labyrinth.components.ActionQueueComponent
import net.abesto.labyrinth.render.AsciiPanelRenderer

object Main {
  def main(args: Array[String]): Unit = {
    val engine = new Engine
    val world = new World(engine)

    val renderer = AsciiPanelRenderer()
    engine.addSystem(renderer)

    val app = new ApplicationMain(renderer)
    app.setup(action => {
      EngineAccessors.player(engine).getComponent(classOf[ActionQueueComponent]).actions.enqueue(action)
      engine.update(1)
    })

    //engine.getSystem(classOf[MapLoaderSystem]).load("sighttest")
    engine.update(1)
  }
}
