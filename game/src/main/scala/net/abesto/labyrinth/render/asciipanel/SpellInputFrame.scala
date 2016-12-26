package net.abesto.labyrinth.render.asciipanel

import asciiPanel.AsciiPanel
import net.abesto.labyrinth.events.StartCastingEvent
import net.mostlyoriginal.api.event.common.Subscribe
import squidpony.squidmath.Coord

import scala.util.Random

class SpellInputFrame(panel: AsciiPanel, topLeft: Coord, size: Coord) extends AsciiPanelFrame(panel, topLeft, size) {
  var show: Boolean = false
  var prompt: String = ""
  var input: String = ""

  val promptOptions: Seq[String] = Seq("Cast", "Invoke", "Mumble", "Incant", "Whisper", "Chant")
  def randomPrompt: String = promptOptions(Random.nextInt(promptOptions.length))

  @Subscribe
  def startCasting(e: StartCastingEvent): Unit = {
    show = true
    prompt = randomPrompt
    input = ""
  }
}
