package net.abesto.labyrinth.systems

import net.abesto.labyrinth.InputMap
import net.abesto.labyrinth.events.{ActivateInputMapEvent, DeactivateInputMapEvent, HidePopupEvent, ShowPopupEvent}
import net.abesto.labyrinth.macros.{SubscribeDeferred, SubscribeDeferredContainer}
import net.mostlyoriginal.api.event.common.EventSystem

@SubscribeDeferredContainer
class PopupInputMapSystem extends EventHandlerSystem {
  var eventSystem: EventSystem = _

  @SubscribeDeferred
  def show(e: ShowPopupEvent): Unit = {
    eventSystem.dispatch(ActivateInputMapEvent(InputMap.popupInputMap))
  }

  @SubscribeDeferred
  def hide(e: HidePopupEvent): Unit = {
    eventSystem.dispatch(DeactivateInputMapEvent(InputMap.popupInputMap))
  }
}
