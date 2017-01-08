package net.abesto.labyrinth.events

import net.abesto.labyrinth.systems.EntityFactory
import net.mostlyoriginal.api.event.common.Event
import squidpony.squidmath.Coord

case class EditorPlaceItemEvent(factoryMethod: EntityFactory => Coord => Int) extends Event

