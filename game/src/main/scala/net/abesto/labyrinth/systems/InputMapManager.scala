package net.abesto.labyrinth.systems

import net.abesto.labyrinth.InputMap
import net.abesto.labyrinth.InputMap.InputMap
import net.abesto.labyrinth.events._
import net.abesto.labyrinth.macros.{SubscribeDeferred, SubscribeDeferredContainer}
import net.mostlyoriginal.api.event.common.EventSystem

@SubscribeDeferredContainer
class InputMapManager extends EventHandlerSystem {
  var eventSystem: EventSystem = _

  protected def activate(m: InputMap): Unit = {
    eventSystem.dispatch(ActivateInputMapEvent(m))
  }
  protected def deactivate(m: InputMap): Unit = {
    eventSystem.dispatch(DeactivateInputMapEvent(m))
  }

  @SubscribeDeferred def showPopup(e: ShowPopupEvent): Unit = activate(InputMap.popupInputMap)
  @SubscribeDeferred def hidePopup(e: HidePopupEvent): Unit = deactivate(InputMap.popupInputMap)

  @SubscribeDeferred def startCasting(e: SpellInputStartEvent): Unit = activate(InputMap.spellCastingInputMap)
  @SubscribeDeferred def abortCasting(e: SpellInputAbortEvent): Unit = deactivate(InputMap.spellCastingInputMap)
  @SubscribeDeferred def finishCasting(e: SpellInputFinishEvent): Unit = deactivate(InputMap.spellCastingInputMap)
}
