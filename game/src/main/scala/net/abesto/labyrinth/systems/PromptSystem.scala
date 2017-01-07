package net.abesto.labyrinth.systems

import net.abesto.labyrinth.components.PromptComponent
import net.abesto.labyrinth.events._
import net.abesto.labyrinth.fsm.InStates
import net.abesto.labyrinth.fsm.States.PromptState
import net.abesto.labyrinth.macros.{DeferredEventHandlerSystem, SubscribeDeferred}

@InStates(Array(classOf[PromptState]))
@DeferredEventHandlerSystem
class PromptSystem extends LabyrinthBaseSystem {
  var helpers: Helpers = _

  def prompt: PromptComponent = helpers.prompt

  @SubscribeDeferred
  def applyOperation(e: PromptInputEvent): Unit = {
    val p = e.op(prompt.input, prompt.cursorPosition)
    prompt.input = p._1
    prompt.cursorPosition = math.min(prompt.input.length, math.max(0, p._2))
  }
}

