package net.abesto.labyrinth

import com.badlogic.ashley.core.Engine
import net.abesto.labyrinth.actions.WalkAction
import net.abesto.labyrinth.components.ActionQueueComponent
import net.abesto.labyrinth.render.AsciiPanelRenderer

object Main {
  def main(args: Array[String]): Unit = {
    val engine = new Engine
    val world = new World(engine)
    val renderer = AsciiPanelRenderer(engine, 80, 24)
    renderer.render()

    val app = new ApplicationMain(renderer)
    app.setVisible(true)

    0.to(5).foreach(n => {
      Thread.sleep(1000)
      world.player.getComponent(classOf[ActionQueueComponent]).actions.enqueue(WalkAction(1, 0))
      engine.update(1)
      println(world.player.getComponent(classOf[ActionQueueComponent]).actions)
      renderer.render()
      println(n)
    })
  }
}
