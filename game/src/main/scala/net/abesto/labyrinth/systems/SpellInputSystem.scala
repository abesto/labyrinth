package net.abesto.labyrinth.systems

import com.artemis.{ComponentMapper, Entity}
import com.artemis.managers.TagManager
import net.abesto.labyrinth.Constants
import net.abesto.labyrinth.components.SpellInputComponent
import net.abesto.labyrinth.events.{SpellInputAbortEvent, SpellInputOperationEvent, SpellInputFinishEvent, SpellInputStartEvent}
import net.abesto.labyrinth.macros.{SubscribeDeferred, SubscribeDeferredContainer}

import scala.util.Random

@SubscribeDeferredContainer
class SpellInputSystem extends EventHandlerSystem {
  var tagManager: TagManager = _
  var spellInputMapper: ComponentMapper[SpellInputComponent] = _

  def spellInputEntityId: Int = tagManager.getEntityId(Constants.Tags.spellInput)
  def spellInput: SpellInputComponent = spellInputMapper.get(spellInputEntityId)

  val promptOptions: Seq[String] = Seq("Cast", "Invoke", "Mumble", "Incant", "Whisper", "Chant")
  def randomPrompt: String = promptOptions(Random.nextInt(promptOptions.length))

  @SubscribeDeferred
  def startCasting(e: SpellInputStartEvent): Unit = {
    spellInput.isActive = true
    spellInput.prompt = randomPrompt
    spellInput.input = ""
    spellInput.cursorPosition = 0
  }

  @SubscribeDeferred
  def applyOperation(e: SpellInputOperationEvent): Unit = {
    val p = e.op(spellInput.input, spellInput.cursorPosition)
    spellInput.input = p._1
    spellInput.cursorPosition = math.min(spellInput.input.length, math.max(0, p._2))
  }

  @SubscribeDeferred
  def abort(e: SpellInputAbortEvent): Unit = {
    spellInput.isActive = false
  }

  @SubscribeDeferred
  def finish(e: SpellInputFinishEvent): Unit = {
    spellInput.isActive = false
    // TODO emit another event with the full spell
  }
}

