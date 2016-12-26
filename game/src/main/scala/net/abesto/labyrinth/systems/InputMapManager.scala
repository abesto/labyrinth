package net.abesto.labyrinth.systems

import javax.swing.KeyStroke

import net.abesto.labyrinth.InputMap
import net.abesto.labyrinth.InputMap.Action
import net.abesto.labyrinth.events._
import net.abesto.labyrinth.macros.{SubscribeDeferred, SubscribeDeferredContainer}
import net.mostlyoriginal.api.event.common.EventSystem

@SubscribeDeferredContainer
class InputMapManager extends EventHandlerSystem {
  var eventSystem: EventSystem = _

  protected def activate(m: Map[KeyStroke, Action]): Unit = {
    eventSystem.dispatch(ActivateInputMapEvent(m))
  }
  protected def deactivate(m: Map[KeyStroke, Action]): Unit = {
    eventSystem.dispatch(DeactivateInputMapEvent(m))
  }

  @SubscribeDeferred def showPopup(e: ShowPopupEvent): Unit = activate(InputMap.popupInputMap)
  @SubscribeDeferred def hidePopup(e: HidePopupEvent): Unit = deactivate(InputMap.popupInputMap)

  @SubscribeDeferred def startCasting(e: StartCastingEvent): Unit = activate(InputMap.spellCastingInputMap)
  @SubscribeDeferred def abortCasting(e: AbortCastingEvent): Unit = deactivate(InputMap.spellCastingInputMap)
  @SubscribeDeferred def finishCasting(e: FinishCastingEvent): Unit = deactivate(InputMap.spellCastingInputMap)
}
