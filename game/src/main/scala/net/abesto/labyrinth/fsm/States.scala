package net.abesto.labyrinth.fsm

import net.abesto.labyrinth.components.PopupComponent


object States {
  sealed class State protected ()

  sealed class MainMenuState extends State {
    var selectedItem: Int = 0
  }
  sealed trait PromptState extends State
  sealed trait PromptAsStatusState extends State

  sealed class EditorState extends State with PromptAsStatusState
  sealed class TileEditorState extends EditorState
  sealed class EditorExtendedModeState extends EditorState with PromptState
  sealed class ItemEditorState extends EditorState
  sealed class PopupEditorState extends EditorState

  sealed class GameState extends State
  sealed class GameMazeState extends GameState
  sealed class GameSpellInputState extends GameState with PromptState
  sealed class GamePopupState extends GameState

  // Iterate over all the states and generate one instance per class
  import scala.reflect.runtime.universe.{ClassSymbol, Mirror, runtimeMirror => ru}
  private val rum: Mirror = ru(getClass.getClassLoader)
  private def allKnownSublasses(cs: ClassSymbol): Set[ClassSymbol] = {
    val direct: Set[ClassSymbol] = cs.knownDirectSubclasses.map(_.asInstanceOf[ClassSymbol])
    direct ++ direct.flatMap(allKnownSublasses)
  }
  private val instances: Map[Class[_ <: State], State] = allKnownSublasses(rum.reflectClass(rum.typeOf[State].typeSymbol.asClass).symbol)
    .filterNot(_.isTrait).map(
    symbol => {
      val cls = rum.runtimeClass(symbol).asSubclass(classOf[State])
      cls -> cls.newInstance()
    }
  ).toMap

  // Get a state
  def apply[T <: State](implicit mf: Manifest[T]): T = instances(mf.runtimeClass.asInstanceOf[Class[T]]).asInstanceOf[T]
}

object Transitions {
  import States._

  // Main menu
  class NewGameEvent extends Transition[MainMenuState, GameMazeState]
  class LoadGameEvent extends Transition[MainMenuState, MainMenuState]  // Not implemented yet, come right back to the menu
  class MainMenuQuitEvent extends Transition[MainMenuState, MainMenuState]   // Not implemented yet, come right back to the menu

  // Editor - open, close
  class OpenEditorEvent extends Transition[MainMenuState, EditorState]
  class CloseEditorEvent extends Transition[EditorState, MainMenuState]
  // Editor - tile editor
  class OpenTileEditorEvent extends Transition[EditorState, TileEditorState]
  class CloseTileEditorEvent extends Transition[TileEditorState, EditorState]
  // Editor - extended mode
  class EditorOpenExtendedModeEvent extends Transition[EditorState, EditorExtendedModeState]
  class EditorAbortExtendedModeEvent extends Transition[EditorExtendedModeState, EditorState]
  class EditorExecuteExtendedModeEvent extends Transition[EditorExtendedModeState, EditorState]
  // Editor - item editor
  class OpenItemEditorEvent extends Transition[EditorState, ItemEditorState]
  class CloseItemEditorEvent extends Transition[ItemEditorState, EditorState]
  // Editor - popup editor
  class OpenPopupEditorEvent extends Transition[ItemEditorState, PopupEditorState]
  class ClosePopupEditorEvent extends Transition[PopupEditorState, ItemEditorState]

  // Casting spells
  class SpellInputStartEvent extends Transition[GameMazeState, GameSpellInputState]
  class SpellInputAbortEvent extends Transition[GameSpellInputState, GameMazeState]
  class SpellInputFinishEvent extends Transition[GameSpellInputState, GameMazeState]

  // Popups during game
  class ShowPopupEvent(var popup: PopupComponent) extends Transition[GameMazeState, GamePopupState]
  class HidePopupEvent extends Transition[GamePopupState, GameMazeState]
}
