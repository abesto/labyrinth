package net.abesto.labyrinth

import net.abesto.labyrinth.fsm.States._
import net.abesto.labyrinth.fsm.Transitions._
import net.abesto.labyrinth.fsm._

object Constants {
  val mazeWidth = 70
  val mazeHeight = 40

  val messageAreaHeight = 5
  val castingPromptHeight = 1

  val fullWidth: Int = mazeWidth
  val fullHeight: Int = mazeHeight + messageAreaHeight + castingPromptHeight

  val sightRadius = 10

  val initialState: MainMenuState = States[MainMenuState]

  object Tags {
    val state = "STATE"
    val maze = "MAZE"
    val player = "PLAYER"
    val spellInput = "SPELL_INPUT"
  }

  val mainMenuItems: Seq[(String, Transition[_, _])] = Seq(
    "New Game" -> new NewGameEvent,
    "Load Game" -> new LoadGameEvent,
    "Level Editor" -> new OpenEditorEvent,
    "Quit" -> new MainMenuQuitEvent
  )
}
