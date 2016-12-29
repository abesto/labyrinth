package net.abesto.labyrinth.systems

import com.artemis.BaseSystem
import net.abesto.labyrinth.fsm.Transition
import net.abesto.labyrinth.macros.{DeferredEventHandlerSystem, SubscribeDeferred}

@DeferredEventHandlerSystem
class StateTransitionSystem extends BaseSystem {
  var helpers: Helpers = _

  @SubscribeDeferred
  def transition(t: Transition): Unit = {
    helpers.state.current = t(helpers.state.current)
  }
}
