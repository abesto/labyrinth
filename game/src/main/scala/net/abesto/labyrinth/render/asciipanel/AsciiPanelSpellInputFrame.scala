package net.abesto.labyrinth.render.asciipanel

import java.awt.Color

import asciiPanel.AsciiPanel
import net.abesto.labyrinth.events.{AbortCastingEvent, FinishCastingEvent, StartCastingEvent, TypingSpellEvent}
import net.mostlyoriginal.api.event.common.Subscribe
import squidpony.squidmath.Coord

import scala.util.Random

class AsciiPanelSpellInputFrame(panel: AsciiPanel, topLeft: Coord, size: Coord) extends AsciiPanelFrame(panel, topLeft, size) {
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

  @Subscribe
  def typeSpellCharacter(e: TypingSpellEvent): Unit = {
    input :+= e.char
  }

  @Subscribe
  def abort(e: AbortCastingEvent): Unit = {
    show = false
  }

  @Subscribe
  def finish(e: FinishCastingEvent): Unit = {
    show = false
    // TODO emit another event with the full spell
  }

  def render(): Unit = {
    clear(Color.black)
    if (show) {
      write(s"$prompt: $input", 0, 0, Color.white, Color.black)
    }
  }
}
