package net.abesto.labyrinth.systems

import com.badlogic.ashley.core.{ComponentMapper, Entity}
import com.badlogic.ashley.signals.Signal
import net.abesto.labyrinth.EngineAccessors
import net.abesto.labyrinth.components.{PositionComponent, WetComponent}
import net.abesto.labyrinth.maze.ShallowWaterTile
import net.abesto.labyrinth.signals.Signals

class ShallowWaterMakesWet extends EventHandlerSystem[Entity] {
  subscribe(Signals.hasWalked)
  val pm: ComponentMapper[PositionComponent] = ComponentMapper.getFor(classOf)
  val wm: ComponentMapper[WetComponent] = ComponentMapper.getFor(classOf)

  override protected def handle(deltaTime: Float, signal: Signal[Entity], entity: Entity): Unit = signal match {
    case Signals.hasWalked =>
      val maze = EngineAccessors.maze(getEngine)
      val position = pm.get(entity)
      val tile = maze.tile(position)
      if (tile.isInstanceOf[ShallowWaterTile] && !wm.has(entity)) {
        entity.add(new WetComponent)
        // The following should be handled by a separate observation system, but until https://github.com/libgdx/ashley/pull/90
        // is merged, it's not quite feasible
        Signals.message.dispatch("You wade into the shallow water. Your clothes are soaked!")
      }
  }
}
