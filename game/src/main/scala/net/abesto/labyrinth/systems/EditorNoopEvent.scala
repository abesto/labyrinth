package net.abesto.labyrinth.systems

import net.mostlyoriginal.api.event.common.Event

// Represents a situation where a keypress could've caused an event, but the world / editor state makes it meaningless
// For example: editing the title of a book, when no book is selected
class EditorNoopEvent extends Event

