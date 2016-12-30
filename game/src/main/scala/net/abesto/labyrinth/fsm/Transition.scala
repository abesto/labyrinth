package net.abesto.labyrinth.fsm

import net.abesto.labyrinth.fsm.States.State
import net.mostlyoriginal.api.event.common.Event

abstract class Transition[From <: State, To <: State](implicit fromMf: Manifest[From], toMf: Manifest[To]) extends Event {
  def apply(current: State): State = States(toMf).ensuring(fromMf.runtimeClass.isInstance(current))
}

