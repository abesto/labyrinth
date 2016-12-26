package net.abesto.labyrinth.events

import javax.swing.KeyStroke

import net.abesto.labyrinth.InputMap.Action
import net.mostlyoriginal.api.event.common.Event

case class ActivateInputMapEvent(inputMap: Map[KeyStroke, Action]) extends Event {

}
