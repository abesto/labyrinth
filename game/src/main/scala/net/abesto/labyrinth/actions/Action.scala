package net.abesto.labyrinth.actions

import com.badlogic.ashley.core.{Engine, Entity}

trait Action {
  def apply(engine: Engine, entity: Entity): Unit
}
