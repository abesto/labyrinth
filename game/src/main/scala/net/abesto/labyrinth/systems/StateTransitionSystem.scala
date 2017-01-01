package net.abesto.labyrinth.systems

import com.artemis.SystemInvocationStrategy
import net.abesto.labyrinth.fsm.States.State
import net.abesto.labyrinth.fsm.Transition
import net.mostlyoriginal.api.event.common.Subscribe

class StateTransitionSystem extends LabyrinthBaseSystem {
  var helpers: Helpers = _

  @Subscribe
  def transition(t: Transition[_ <: State, _ <: State]): Unit = {
    helpers.state.current = t(helpers.state.current)
    processSystem()
  }

  override def processSystem(): Unit = {
    super.processSystem()
    val strat: SystemInvocationStrategy = world.getInvocationStrategy
    world.getSystems.forEach((s) => strat.setEnabled(s, helpers.isEnabledInCurrentState(s)))
  }
}
