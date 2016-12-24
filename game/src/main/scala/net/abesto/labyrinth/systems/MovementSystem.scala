package net.abesto.labyrinth.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.signals.Signal
import net.abesto.labyrinth.components.PositionComponent
import net.abesto.labyrinth.{Constants, EngineAccessors}
import net.abesto.labyrinth.signals.{MoveData, Signals}

import scala.io.Source

class MovementSystem extends EventHandlerSystem[MoveData] {
  subscribe(Signals.tryWalking)
  val pm: ComponentMapper[PositionComponent] = ComponentMapper.getFor(classOf[PositionComponent])

  def withinBounds(p: PositionComponent): Boolean = p.coord.isWithin(Constants.mazeWidth, Constants.mazeHeight)

  override protected def handle(deltaTime: Float, signal: Signal[MoveData], data: MoveData): Unit = signal match {
    case Signals.`tryWalking` =>
      val maze = EngineAccessors.maze(getEngine)
      val oldPosition = pm.get(data.what)
      val newPosition = oldPosition + data.vector
      val canWalk = withinBounds(newPosition) && maze.tile(newPosition).canBeStoodOn
      if (canWalk) {
        data.what.add(newPosition)
        Signals.hasWalked.dispatch(data.what)
      }
  }
}
