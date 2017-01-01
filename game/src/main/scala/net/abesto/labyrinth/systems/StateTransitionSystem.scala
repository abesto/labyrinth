package net.abesto.labyrinth.systems

import net.abesto.labyrinth.fsm.States.State
import net.abesto.labyrinth.fsm.Transition
import net.abesto.labyrinth.macros.{DeferredEventHandlerSystem, SubscribeDeferred}

@DeferredEventHandlerSystem
class StateTransitionSystem extends InstrumentedSystem {
  var helpers: Helpers = _

  @SubscribeDeferred
  def transition(t: Transition[_ <: State, _ <: State]): Unit = {
    helpers.state.current = t(helpers.state.current)
  }
}
