package net.abesto.labyrinth.magic
import net.abesto.labyrinth.Helpers
import net.abesto.labyrinth.events.MessageEvent
import net.mostlyoriginal.api.event.common.EventSystem
import squidpony.squidmath.Coord

class TestEffect extends SpellEffect {
  var eventSystem: EventSystem = _

  override protected def applyToEntities(target: Seq[Int]): Unit = target.foreach(
    t => eventSystem.dispatch(MessageEvent(s"Cast test spell at entity $t"))
  )

  override protected def applyToMaze(target: Seq[Coord]): Unit = target.foreach(
    c => {
      eventSystem.dispatch(MessageEvent(s"Cast test spell at tile $c"))
      Helpers.maze(world).tile(c).char = '!'
    }
  )
}
