package net.abesto.labyrinth.fsm


object States {
  sealed class State protected ()

  sealed class MainMenuState extends State {
    var selectedItem: Int = 0
  }
  sealed trait PromptState extends State

  sealed class EditorState extends State
  sealed class TileEditorState extends EditorState
  sealed class EditorOpenMazeState extends EditorState with PromptState

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
  // Editor - Tile editor
  class OpenTileEditorEvent extends Transition[EditorState, TileEditorState]
  class CloseTileEditorEvent extends Transition[TileEditorState, EditorState]
  // Editor - open a maze
  class EditorOpenMazeEvent extends Transition[EditorState, EditorOpenMazeState]
  class EditorAbortOpenMazeEvent extends Transition[EditorOpenMazeState, EditorState]
  class EditorExecuteOpenMazeEvent extends Transition[EditorOpenMazeState, EditorState]

  // Casting spells
  class SpellInputStartEvent extends Transition[GameMazeState, GameSpellInputState]
  class SpellInputAbortEvent extends Transition[GameSpellInputState, GameMazeState]
  class SpellInputFinishEvent extends Transition[GameSpellInputState, GameMazeState]

  // Popups during game
  class ShowPopupEvent(var title: String, var text: String) extends Transition[GameMazeState, GamePopupState]
  class HidePopupEvent extends Transition[GamePopupState, GameMazeState]
}
