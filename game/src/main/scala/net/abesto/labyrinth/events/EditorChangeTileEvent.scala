package net.abesto.labyrinth.events

import net.abesto.labyrinth.maze.MazeTile
import net.mostlyoriginal.api.event.common.Event

class EditorChangeTileEvent[T <: MazeTile](implicit mf: Manifest[T]) extends Event {
  def makeTile(x: Int, y: Int): T =
    mf.runtimeClass.getConstructor(classOf[Int], classOf[Int]).newInstance(x.asInstanceOf[Object], y.asInstanceOf[Object]).asInstanceOf[T]
}

