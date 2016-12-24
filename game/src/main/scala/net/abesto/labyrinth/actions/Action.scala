package net.abesto.labyrinth.actions

import com.badlogic.ashley.core.Engine

trait Action {
  def apply(engine: Engine): Unit
}
