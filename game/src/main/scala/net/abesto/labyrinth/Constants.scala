package net.abesto.labyrinth

import com.artemis.World
import net.abesto.labyrinth.Tiles.Kind._
import net.abesto.labyrinth.events.{EditorChangeTileEvent, EditorGenerateMazeEvent, EditorMoveMazeCursorEvent}
import net.abesto.labyrinth.fsm.States._
import net.abesto.labyrinth.fsm.Transitions._
import net.abesto.labyrinth.fsm._
import net.abesto.labyrinth.ui.InputMap._
import net.mostlyoriginal.api.event.common.Event
import squidpony.squidmath.Coord


object Constants {
  val mazeWidth = 70
  val mazeHeight = 40

  val messageAreaHeight = 5
  val castingPromptHeight = 1

  val sidebarWidth = 40

  val fullWidth: Int = mazeWidth + sidebarWidth
  val fullHeight: Int = mazeHeight + messageAreaHeight + castingPromptHeight

  val sightRadius = 10

  val initialState: MainMenuState = States[MainMenuState]

  object Tags {
    val state = "STATE"
    val maze = "MAZE"
    val player = "PLAYER"
    val prompt = "PROMPT"
  }

  val mainMenuItems: Seq[(String, Transition[_, _])] = Seq(
    "New Game" -> new NewGameEvent,
    "Load Game" -> new LoadGameEvent,
    "Level Editor" -> new OpenEditorEvent,
    "Quit" -> new MainMenuQuitEvent
  )

  case class EditorAction(tile: Tiles.Kind, key: Char, description: String, event: Event) {
    def asInputMapEntry: (Seq[Char], (World) => Event) = Seq(key) -> ((_: World) => event)
  }

  lazy val cursorMazeActions: Seq[EditorAction] = Seq(
    EditorAction(UpArrow, upArrow, "Maze cursor up", EditorMoveMazeCursorEvent(_.add(Coord.get(0, -1)))),
    EditorAction(DownArrow, downArrow, "Maze cursor down", EditorMoveMazeCursorEvent(_.add(Coord.get(0, 1)))),
    EditorAction(LeftArrow, leftArrow, "Maze cursor left", EditorMoveMazeCursorEvent(_.add(Coord.get(-1, 0)))),
    EditorAction(RightArrow, rightArrow, "Maze cursor right", EditorMoveMazeCursorEvent(_.add(Coord.get(1, 0))))
  )

  lazy val editorActions: Map[EditorState, Seq[EditorAction]] = Map(
    States[EditorState] -> (cursorMazeActions ++ Seq(
      EditorAction(AlphaNum('g'), 'g', "Generate new maze", new EditorGenerateMazeEvent),
      EditorAction(AlphaNum('t'), 't', "Edit Tiles", new OpenTileEditorEvent),
      EditorAction(AlphaNum('q'), 'q', "Quit to Main Menu", new CloseEditorEvent)
    )),
    States[TileEditorState] -> (cursorMazeActions ++ Seq(
      EditorAction(WallHash, '#', "Wall", EditorChangeTileEvent(WallHash)),
      EditorAction(SmoothFloor, '.', "Smooth Floor", EditorChangeTileEvent(SmoothFloor)),
      EditorAction(ShallowWater, '~', "Shallow Water", EditorChangeTileEvent(ShallowWater)),
      EditorAction(AlphaNum('q'), 'q', "Back", new CloseTileEditorEvent)
    ))
  )
}
