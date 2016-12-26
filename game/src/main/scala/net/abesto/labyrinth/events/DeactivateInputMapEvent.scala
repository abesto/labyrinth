package net.abesto.labyrinth.events

import javax.swing.KeyStroke

import net.abesto.labyrinth.InputMap.Action
import net.mostlyoriginal.api.event.common.Event

case class DeactivateInputMapEvent(inputMap: Map[KeyStroke, Action]) extends Event {

}
