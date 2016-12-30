package net.abesto.labyrinth.render.asciipanel

import java.awt.Color

import asciiPanel.{AsciiFont, AsciiPanel}
import net.abesto.labyrinth.Constants
import net.abesto.labyrinth.render.{Renderer => RendererTrait}
import net.abesto.labyrinth.systems.Helpers
import net.mostlyoriginal.api.event.common.EventSystem

abstract case class RendererImpl(protected val frames: Frame*) extends RendererTrait {
  var eventSystem: EventSystem = _
  var helpers: Helpers = _

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
    val (enabled, disabled) = frames.partition(helpers.isEnabledInCurrentState(_))
    disabled.foreach(_.clear(Color.black))
    enabled.foreach(_.render())
    panel.repaint()
  }
}
