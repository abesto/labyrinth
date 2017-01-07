package net.abesto.labyrinth.systems

import net.abesto.labyrinth.components.MazeHighlightComponent.Type.SpellTarget
import net.abesto.labyrinth.components.PromptComponent
import net.abesto.labyrinth.events._
import net.abesto.labyrinth.fsm.InStates
import net.abesto.labyrinth.fsm.States.GameState
import net.abesto.labyrinth.fsm.Transitions.{SpellInputAbortEvent, SpellInputFinishEvent, SpellInputStartEvent}
import net.abesto.labyrinth.macros.{DeferredEventHandlerSystem, SubscribeDeferred}
import net.abesto.labyrinth.magic.{Spell, SpellParser}
import net.mostlyoriginal.api.event.common.EventSystem

import scala.util.Random

@InStates(Array(classOf[GameState]))
@DeferredEventHandlerSystem
class SpellInputSystem(parser: SpellParser) extends LabyrinthBaseSystem {
  var eventSystem: EventSystem = _
  var helpers: Helpers = _

  def prompt: PromptComponent = helpers.prompt

  val promptOptions: Seq[String] = Seq("Cast", "Invoke", "Mumble", "Incant", "Whisper", "Chant")
  def randomPrompt: String = promptOptions(Random.nextInt(promptOptions.length))

  def reset(): Unit = {
    prompt.reset()
    helpers.highlight.clear(SpellTarget)
  }

  def parseSpell(): Option[Spell] = parser.parse(prompt.input)

  @SubscribeDeferred
  def startCasting(e: SpellInputStartEvent): Unit = {
    reset()
    prompt.prompt = randomPrompt
  }

  @SubscribeDeferred
  def applyOperation(e: PromptInputEvent): Unit = {
    parseSpell() match {
     case Some(spell) => helpers.highlight.set(SpellTarget, spell.target.affectedTiles(world))
     case None => helpers.highlight.clear(SpellTarget)
   }
  }

  @SubscribeDeferred
  def abort(e: SpellInputAbortEvent): Unit = {
    reset()
  }

  @SubscribeDeferred
  def finish(e: SpellInputFinishEvent): Unit = {
    eventSystem.dispatch(SpellCastEvent(helpers.playerEntityId, parseSpell()))
    reset()
  }
}

