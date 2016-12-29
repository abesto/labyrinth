package net.abesto.labyrinth.fsm

object States {
  sealed class State

  class GameState extends State
  class GameMazeState extends GameState
  class GameSpellInputState extends GameState
  class GamePopupState extends GameState
}

object Transitions {
  import States._

  // Casting spells
  class SpellInputStartEvent extends Transition(new GameMazeState, new GameSpellInputState)
  class SpellInputAbortEvent extends Transition(new GameSpellInputState, new GameMazeState)
  class SpellInputFinishEvent extends Transition(new GameSpellInputState, new GameMazeState)

  // Popups during game
  class ShowPopupEvent(var title: String, var text: String) extends Transition(new GameMazeState, new GamePopupState)
  class HidePopupEvent extends Transition(new GamePopupState, new GameMazeState)
}
