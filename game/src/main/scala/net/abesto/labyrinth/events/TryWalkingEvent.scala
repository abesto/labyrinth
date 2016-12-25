package net.abesto.labyrinth.events

import net.mostlyoriginal.api.event.common.Event
import squidpony.squidmath.Coord

case class TryWalkingEvent(vector: Coord, entityId: Int) extends Event {

}
