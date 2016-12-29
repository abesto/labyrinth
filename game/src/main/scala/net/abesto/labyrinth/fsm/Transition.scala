package net.abesto.labyrinth.fsm

import net.abesto.labyrinth.fsm.States.State
import net.mostlyoriginal.api.event.common.Event

abstract class Transition(from: State, to: State) extends Event {
  def apply(current: State): State = to.ensuring(current.getClass == from.getClass)
}
