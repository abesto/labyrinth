package net.abesto.labyrinth.signals

import com.badlogic.ashley.core.Entity
import net.abesto.labyrinth.components.PositionComponent

case class MoveData(vector: PositionComponent, what: Entity) {}

object MoveData {
  def apply(x: Int, y: Int, what: Entity): MoveData = MoveData(PositionComponent(x, y), what)
}

