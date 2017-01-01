package net.abesto.labyrinth.events

import net.abesto.labyrinth.Tiles
import net.mostlyoriginal.api.event.common.Event

case class EditorChangeTileEvent(kind: Tiles.Kind) extends Event

