package net.abesto.labyrinth.events

import net.abesto.labyrinth.ui.InputMap.InputMap
import net.mostlyoriginal.api.event.common.Event

case class DeactivateInputMapEvent(inputMap: InputMap) extends Event {

}
