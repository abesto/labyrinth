package net.abesto.labyrinth.events

import net.mostlyoriginal.api.event.common.Event
import squidpony.squidmath.Coord

case class EditorMoveMazeCursorEvent(op: (Coord) => Coord) extends Event

