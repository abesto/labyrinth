package net.abesto.labyrinth.magic
import com.artemis.ComponentMapper
import net.abesto.labyrinth.components.WetComponent
import net.abesto.labyrinth.events.MessageEvent
import net.mostlyoriginal.api.event.common.EventSystem
import squidpony.squidmath.Coord

class DryEffect extends SpellEffect {
  var wetMapper: ComponentMapper[WetComponent] = _
  var eventSystem: EventSystem = _

  override protected def applyToEntities(target: Seq[Int]): Unit = target.foreach(entity => {
    // Yes, for now this assumes the only entity this is cast on is the player
    if (!wetMapper.has(entity)) {
      eventSystem.dispatch(MessageEvent("You're already quite dry."))
    } else {
      wetMapper.remove(entity)
      eventSystem.dispatch(MessageEvent("Your clothes dry in a matter of seconds."))
    }
  })

  override protected def applyToMaze(target: Seq[Coord]): Unit = {}
}
