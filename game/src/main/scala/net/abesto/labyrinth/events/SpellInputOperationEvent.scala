package net.abesto.labyrinth.events

import net.mostlyoriginal.api.event.common.Event

case class SpellInputOperationEvent(op: (String, Int) => (String, Int)) extends Event {

}
