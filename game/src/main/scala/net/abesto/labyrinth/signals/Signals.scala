package net.abesto.labyrinth.signals

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.signals.Signal

object Signals {
  val tryWalking = new Signal[MoveData]()
  val hasWalked = new Signal[Entity]()

  val message = new Signal[String]()
}
