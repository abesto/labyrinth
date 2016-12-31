package net.abesto.labyrinth.systems

import javax.swing.KeyStroke

import com.artemis.BaseSystem
import net.abesto.labyrinth.events.KeyEvent
import net.abesto.labyrinth.macros.{DeferredEventHandlerSystem, SubscribeDeferred}
import net.abesto.labyrinth.ui.InputMap
import net.mostlyoriginal.api.event.common.EventSystem

@DeferredEventHandlerSystem
class InputHandlerSystem extends BaseSystem {
  var helpers: Helpers = _
  var eventSystem: EventSystem = _

  @SubscribeDeferred
  def input(e: KeyEvent): Unit = {
    val inputMap = InputMap.inputMap(helpers.state.current)
    inputMap.get(KeyStroke.getKeyStrokeForEvent(e.awt)).foreach(
      f => eventSystem.dispatch(f(world))
    )
  }
}
