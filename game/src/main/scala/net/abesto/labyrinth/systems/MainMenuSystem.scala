package net.abesto.labyrinth.systems

import net.abesto.labyrinth.Constants
import net.abesto.labyrinth.events.{MainMenuMoveEvent, MainMenuSelectedEvent}
import net.abesto.labyrinth.fsm.States.MainMenuState
import net.abesto.labyrinth.macros.{DeferredEventHandlerSystem, SubscribeDeferred}
import net.mostlyoriginal.api.event.common.EventSystem

@DeferredEventHandlerSystem
class MainMenuSystem extends LabyrinthBaseSystem {
  var helpers: Helpers = _
  var eventSystem: EventSystem = _

  def mainMenuState: MainMenuState = helpers.state.current.asInstanceOf[MainMenuState]
  def selectedItem: Int = mainMenuState.selectedItem
  def selectedItem_=(n: Int): Unit = mainMenuState.selectedItem = n

  @SubscribeDeferred
  def move(e: MainMenuMoveEvent): Unit =
    selectedItem = math.min(Constants.mainMenuItems.length - 1, math.max(0, e.op(selectedItem)) )

  @SubscribeDeferred
  def select(e: MainMenuSelectedEvent): Unit =
    eventSystem.dispatch(
      Constants.mainMenuItems(selectedItem)._2
    )
}
