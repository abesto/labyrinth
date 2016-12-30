package net.abesto.labyrinth.events

import net.mostlyoriginal.api.event.common.Event

case class MainMenuMoveEvent(op: (Int) => Int) extends Event

