package net.abesto.labyrinth.signals

import com.badlogic.ashley.signals.Signal

object Signals {
  val walk = new Signal[MoveData]()
}
