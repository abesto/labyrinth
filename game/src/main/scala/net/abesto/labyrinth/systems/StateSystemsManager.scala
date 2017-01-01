package net.abesto.labyrinth.systems

import com.artemis.BaseSystem

// Ensures that systems with @InState annotations are enabled only when the game is in a mentioned state
class StateSystemsManager extends InstrumentedSystem {
  var helpers: Helpers = _
  override def processSystem(): Unit = {
    super.processSystem()
    world.getSystems.forEach((s: BaseSystem) => s.setEnabled(helpers.isEnabledInCurrentState(s)))
  }
}
