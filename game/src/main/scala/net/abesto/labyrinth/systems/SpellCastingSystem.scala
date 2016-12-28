package net.abesto.labyrinth.systems

import com.artemis.BaseSystem
import net.abesto.labyrinth.Helpers
import net.abesto.labyrinth.events.{MessageEvent, SpellCastEvent}
import net.abesto.labyrinth.macros.{DeferredEventHandlerSystem, SubscribeDeferred}
import net.mostlyoriginal.api.event.common.EventSystem

@DeferredEventHandlerSystem
class SpellCastingSystem extends BaseSystem {
  var eventSystem: EventSystem = _

  @SubscribeDeferred
  def cast(e: SpellCastEvent): Unit = {
    val castByPlayer = Helpers.playerEntityId(world) == e.caster
    e.spell match {
      case None =>
        eventSystem.dispatch(MessageEvent(
          if (castByPlayer) {
            "Your spell fizzles"
          } else {
            "Someone messed up a spell"
          }
        ))
      case Some(spell) =>
        val casterString = if (castByPlayer) "You" else "Someone"
        eventSystem.dispatch(MessageEvent(s"$casterString cast $spell"))
        spell.cast(world)
    }
  }
}
