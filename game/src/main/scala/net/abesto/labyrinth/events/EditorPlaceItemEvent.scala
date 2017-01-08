package net.abesto.labyrinth.events

import net.abesto.labyrinth.systems.ItemFactory
import net.mostlyoriginal.api.event.common.Event
import squidpony.squidmath.Coord

case class EditorPlaceItemEvent(factoryMethod: ItemFactory => Coord => Int) extends Event

