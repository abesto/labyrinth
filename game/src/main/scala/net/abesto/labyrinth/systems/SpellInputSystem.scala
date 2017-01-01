package net.abesto.labyrinth.systems

import com.artemis.ComponentMapper
import com.artemis.managers.TagManager
import net.abesto.labyrinth.Constants
import net.abesto.labyrinth.components.MazeHighlightComponent.Type.SpellTarget
import net.abesto.labyrinth.components.SpellInputComponent
import net.abesto.labyrinth.events._
import net.abesto.labyrinth.fsm.InStates
import net.abesto.labyrinth.fsm.States.GameSpellInputState
import net.abesto.labyrinth.fsm.Transitions.{SpellInputAbortEvent, SpellInputFinishEvent, SpellInputStartEvent}
import net.abesto.labyrinth.macros.{DeferredEventHandlerSystem, SubscribeDeferred}
import net.abesto.labyrinth.magic.SpellParser
import net.mostlyoriginal.api.event.common.EventSystem

import scala.util.Random

@InStates(Array(classOf[GameSpellInputState]))
@DeferredEventHandlerSystem
class SpellInputSystem(parser: SpellParser) extends LabyrinthBaseSystem {
  var tagManager: TagManager = _
  var eventSystem: EventSystem = _
  var spellInputMapper: ComponentMapper[SpellInputComponent] = _
  var helpers: Helpers = _

  def spellInputEntityId: Int = tagManager.getEntityId(Constants.Tags.spellInput)
  def spellInput: SpellInputComponent = spellInputMapper.get(spellInputEntityId)

  val promptOptions: Seq[String] = Seq("Cast", "Invoke", "Mumble", "Incant", "Whisper", "Chant")
  def randomPrompt: String = promptOptions(Random.nextInt(promptOptions.length))

  def reset(): Unit = {
    spellInput.input = ""
    spellInput.cursorPosition = 0
    spellInput.spell = None
    helpers.highlight.clear(SpellTarget)
  }

  @SubscribeDeferred
  def startCasting(e: SpellInputStartEvent): Unit = {
    spellInput.prompt = randomPrompt
    reset()
  }

  @SubscribeDeferred
  def applyOperation(e: SpellInputOperationEvent): Unit = {
    val p = e.op(spellInput.input, spellInput.cursorPosition)
    spellInput.input = p._1
    spellInput.cursorPosition = math.min(spellInput.input.length, math.max(0, p._2))
    spellInput.spell = parser.parse(spellInput.input)

   spellInput.spell match {
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
    eventSystem.dispatch(SpellCastEvent(helpers.playerEntityId, spellInput.spell))
    reset()
  }
}

