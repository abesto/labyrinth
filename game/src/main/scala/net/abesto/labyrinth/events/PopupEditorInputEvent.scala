package net.abesto.labyrinth.events

import net.abesto.labyrinth.systems.EditorSystem.PopupEditorData
import net.mostlyoriginal.api.event.common.Event

case class PopupEditorInputEvent(op: (PopupEditorData) => Unit) extends Event

