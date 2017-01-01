package net.abesto.labyrinth.events

import net.mostlyoriginal.api.event.common.Event

case class KeyPressedEvent(char: Char) extends Event {
  override def toString: String = s"KeyPressedEvent(${char.toInt})"
}

