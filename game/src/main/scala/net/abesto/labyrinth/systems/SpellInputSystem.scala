package net.abesto.labyrinth.systems

import com.artemis.annotations.Wire
import com.artemis.managers.TagManager
import com.artemis.{BaseSystem, ComponentMapper}
import net.abesto.labyrinth.Constants
import net.abesto.labyrinth.components.SpellInputComponent
import net.abesto.labyrinth.events._
import net.abesto.labyrinth.macros.{DeferredEventHandlerSystem, SubscribeDeferred}
import net.abesto.labyrinth.magic.SpellParser
import net.mostlyoriginal.api.event.common.EventSystem

import scala.util.Random

@DeferredEventHandlerSystem
class SpellInputSystem(parser: SpellParser) extends BaseSystem {
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
  }

  @SubscribeDeferred
  def startCasting(e: SpellInputStartEvent): Unit = {
    spellInput.isActive = true
    spellInput.prompt = randomPrompt
    reset()
  }

  @SubscribeDeferred
  def applyOperation(e: SpellInputOperationEvent): Unit = {
    val p = e.op(spellInput.input, spellInput.cursorPosition)
    spellInput.input = p._1
    spellInput.cursorPosition = math.min(spellInput.input.length, math.max(0, p._2))
    spellInput.spell = parser.parse(spellInput.input)
  }

  @SubscribeDeferred
  def abort(e: SpellInputAbortEvent): Unit = {
    spellInput.isActive = false
    reset()
  }

  @SubscribeDeferred
  def finish(e: SpellInputFinishEvent): Unit = {
    spellInput.isActive = false
    eventSystem.dispatch(SpellCastEvent(helpers.playerEntityId, spellInput.spell))
    reset()
  }
}

