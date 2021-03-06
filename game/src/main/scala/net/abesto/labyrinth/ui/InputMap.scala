package net.abesto.labyrinth.ui

import java.awt.event.KeyEvent

import com.artemis.World
import com.artemis.managers.TagManager
import net.abesto.labyrinth.Constants
import net.abesto.labyrinth.events._
import net.abesto.labyrinth.fsm.States
import net.abesto.labyrinth.fsm.States._
import net.abesto.labyrinth.fsm.Transitions._
import net.abesto.labyrinth.systems.EditorSystem
import net.mostlyoriginal.api.event.common.{Event, EventSystem}
import squidpony.squidmath.Coord

import scala.language.implicitConversions

object InputMap {
  type InputMapKey = Seq[Char]
  type InputMapValue = (World => Event)
  type InputMap = Map[InputMapKey, InputMapValue]
  type InputMapEntry = (InputMapKey, InputMapValue)

  implicit def int(n: Int): Char = n.toChar
  implicit def ks(c: Char): InputMapKey = Seq(c)
  implicit def e(evt: Event): InputMapValue = (w: World) => evt
  implicit def tu(t: (Char, Event)): InputMapEntry = (t._1, t._2)

  val leftArrow: Char = KeyEvent.VK_LEFT
  val rightArrow: Char = KeyEvent.VK_RIGHT
  val upArrow: Char = KeyEvent.VK_UP
  val downArrow: Char = KeyEvent.VK_DOWN
  val space: Char = KeyEvent.VK_SPACE
  val enter: Char = KeyEvent.VK_ENTER
  val escape: Char = KeyEvent.VK_ESCAPE
  val backspace: Char = KeyEvent.VK_BACK_SPACE
  val delete: Char = KeyEvent.VK_DELETE

  protected def walk(x: Int, y: Int): (World) => Event =
    (w: World) => TryWalkingEvent(
      Coord.get(x, y),
      w.getSystem(classOf[TagManager]).getEntityId(Constants.Tags.player)
    )

  protected val editorActionsInputMap: Map[State, InputMap] =
    Constants.editorActions.map(
      p => p._1 -> p._2.filterNot(_.event == null).map(_.asInputMapEntry).toMap
    )

  protected def promptInputMap(ps: (InputMapKey, InputMapValue)*): Map[InputMapKey, InputMapValue] = ps.toMap ++ (
    ('a'.to('z') ++ 'A'.to('Z') ++ Seq(' ', '-', '/') ++ '0'.to('9')).map(c => ks(c) ->
      e(PromptInputEvent((s, cp) => (s.take(cp) + c + s.drop(cp), cp + 1)))
    ).toMap ++ Map[InputMapKey, InputMapValue](
      backspace -> PromptInputEvent((s, cp) => (s.take(cp - 1) + s.drop(cp), cp - 1)),
      leftArrow -> PromptInputEvent((s, cp) => (s, cp - 1)),
      rightArrow -> PromptInputEvent((s, cp) => (s, cp + 1)),
      delete -> PromptInputEvent((s, cp) => (s.take(cp) + s.drop(cp + 1), cp))
    ))

  protected def popupTitleEditorInputMap(ps: (InputMapKey, InputMapValue)*): InputMap =
    32.to(126).map(_.toChar).map(c => ks(c) -> e(PopupEditorInputEvent(s => {
      val t = s.popup.title
      if (t.length < Constants.popupTextMaxWidth) {
        s.popup.title = t.take(s.cursorPosition.getX) + c + t.drop(s.cursorPosition.getX)
        s.cursorPosition = s.cursorPosition.setX(s.cursorPosition.getX + 1)
      }
    }))).toMap ++ ps.toMap

  protected def popupTextEditorInputMap(ps: (InputMapKey, InputMapValue)*): InputMap =
    32.to(126).map(_.toChar).map(c => ks(c) -> e(PopupEditorInputEvent(s => {
      val t = s.popup.text
      s.popup.text = t.take(s.cursorPosition.getX) + c + t.drop(s.cursorPosition.getX)
      if (s.cursorPosition.getX == Constants.popupTextMaxWidth && s.cursorPosition.getY < Constants.popupTextMaxHeight) {
        s.cursorPosition = Coord.get(0, s.cursorPosition.getY + 1)
        s.popup.text += '\n'
      } else if (s.cursorPosition.getX < Constants.popupTextMaxWidth) {
        s.cursorPosition = s.cursorPosition.setX(s.cursorPosition.getX + 1)
      }
    }))).toMap ++ ps.toMap

  protected val rawInputMap: Map[State, InputMap] = Map(
    States[GameMazeState] -> Map(
      Seq(leftArrow, 'h') -> walk(-1, 0),
      Seq(rightArrow, 'l') -> walk(1, 0),
      Seq(downArrow, 'j') -> walk(0, 1),
      Seq(upArrow, 'k') -> walk(0, -1),
      'z' -> new SpellInputStartEvent
    ),
    States[GamePopupState] -> Map(
      space -> new HidePopupEvent
    ),
    States[GameSpellInputState] -> promptInputMap(
      enter -> new SpellInputFinishEvent,
      escape -> new SpellInputAbortEvent
    ),
    States[EditorExtendedModeState] -> promptInputMap(
      enter -> new EditorExecuteExtendedModeEvent,
      escape -> new EditorAbortExtendedModeEvent
    ),
    States[MainMenuState] -> Map(
      upArrow -> MainMenuMoveEvent(_ - 1),
      downArrow -> MainMenuMoveEvent(_ + 1),
      enter -> new MainMenuSelectedEvent
    ),
    States[PopupTitleEditorState] -> popupTitleEditorInputMap(
      leftArrow -> PopupEditorInputEvent(s => s.cursorPosition = s.cursorPosition.setX(math.max(0, s.cursorPosition.getX - 1))),
      rightArrow -> PopupEditorInputEvent(s => s.cursorPosition  = s.cursorPosition.setX(math.min(s.popup.title.length, s.cursorPosition.getX + 1))),
      downArrow -> new PopupEditorTextEvent
    ),
    States[PopupTextEditorState] -> popupTextEditorInputMap(
      leftArrow -> PopupEditorInputEvent(s => s.cursorPosition = s.cursorPosition.setX(math.max(0, s.cursorPosition.getX - 1))),
      rightArrow -> PopupEditorInputEvent(s => s.cursorPosition  = s.cursorPosition.setX(math.min(s.popup.title.length, s.cursorPosition.getX + 1))),
      ks(upArrow) -> ((w: World) =>
        if (w.getSystem(classOf[EditorSystem]).popupEditorState.cursorPosition.getY == 0) {
          new PopupEditorTitleEvent
        } else {
          PopupEditorInputEvent(s => s.cursorPosition = Coord.get(
            math.min(s.cursorPosition.getX, s.popup.text.lines.drop(s.cursorPosition.getY - 1).next.length),
            s.cursorPosition.getY - 1
          ))
        }
      ),
      ks(downArrow) -> ((w: World) => {
        val s = w.getSystem(classOf[EditorSystem]).popupEditorState
        if (s.cursorPosition.getY == s.popup.text.lines.length - 1) {
          // Save button is on the left
          if (s.cursorPosition.getX <= math.min(Constants.popupTextMaxWidth, s.popup.text.lines.map(_.length).max)) {
            new PopupEditorHoverSaveEvent
          } else {
            new PopupEditorHoverCancelEvent
          }
        } else {
          PopupEditorInputEvent(s => s.cursorPosition = Coord.get(
            math.min(s.cursorPosition.getX, s.popup.text.lines.drop(s.cursorPosition.getY + 1).next.length),
            s.cursorPosition.getY + 1
          ))
        }
      })
    )
  )

  val inputMap: Map[State, Map[Char, (World) => Event]] = {
    val x = for {
      state <- rawInputMap.keySet ++ editorActionsInputMap.keySet
      raw = rawInputMap.getOrElse(state, Map.empty)
      editor = editorActionsInputMap.getOrElse(state, Map.empty)
    } yield {
      state -> (raw ++ editor)
    }
    x.toMap.mapValues(_.flatMap(p => p._1.map(_ -> p._2)))
  }

  protected def dispatchEvent(e: Event): (World) => Unit =
    (w: World) => w.getSystem(classOf[EventSystem]).dispatch(e)

  def actionMap(world: World, inputMap: InputMap): Map[InputMapValue, (World) => Unit] =
    inputMap.values.map(v => v -> dispatchEvent(v(world))).toMap
}
