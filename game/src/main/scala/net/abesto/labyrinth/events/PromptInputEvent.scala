package net.abesto.labyrinth.events

import net.mostlyoriginal.api.event.common.Event

case class PromptInputEvent(op: (String, Int) => (String, Int)) extends Event {

}
