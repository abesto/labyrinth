package net.abesto.labyrinth.render.asciipanel

import asciiPanel.{AsciiFont, AsciiPanel}
import net.abesto.labyrinth.Constants
import net.abesto.labyrinth.render.{Renderer => RendererTrait}
import net.mostlyoriginal.api.event.common.EventSystem

case class Renderer(protected val frames: Frame*) extends RendererTrait {
  protected var eventSystem: EventSystem = _

  val panel = new AsciiPanel(
    Constants.fullWidth,
    Constants.fullHeight,
    AsciiFont.TALRYTH_15_15)

  protected def inject[T <: Frame](o: T): Unit = {
    o.world = world
    o.panel = panel
    world.inject(o)
    eventSystem.registerEvents(o)
  }

  override def initialize(): Unit = {
    super.setWorld(world)
    frames.foreach(inject)
  }

  override def processSystem(): Unit = {
    frames.foreach(_.render())
    panel.repaint()
  }
}
