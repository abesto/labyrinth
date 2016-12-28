package net.abesto.labyrinth.events

import net.abesto.labyrinth.magic.Spell
import net.mostlyoriginal.api.event.common.Event

case class SpellCastEvent(caster: Int, spell: Option[Spell]) extends Event
