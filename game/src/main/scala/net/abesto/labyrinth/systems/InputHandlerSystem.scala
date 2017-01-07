package net.abesto.labyrinth.systems

import net.abesto.labyrinth.events.KeyPressedEvent
import net.abesto.labyrinth.macros.{DeferredEventHandlerSystem, SubscribeDeferred}
import net.abesto.labyrinth.ui.InputMap
import net.mostlyoriginal.api.event.common.EventSystem

@DeferredEventHandlerSystem
class InputHandlerSystem extends LabyrinthBaseSystem {
  var helpers: Helpers = _
  var eventSystem: EventSystem = _

  @SubscribeDeferred
  def input(e: KeyPressedEvent): Unit = {
    val inputMap = InputMap.inputMap(helpers.state.current)
    inputMap.get(e.char).foreach(
      f => eventSystem.dispatch(f(world))
    )
  }
}
