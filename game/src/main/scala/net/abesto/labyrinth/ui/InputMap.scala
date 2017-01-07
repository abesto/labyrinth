package net.abesto.labyrinth.ui

import java.awt.event.KeyEvent

import com.artemis.World
import com.artemis.managers.TagManager
import net.abesto.labyrinth.Constants
import net.abesto.labyrinth.events._
import net.abesto.labyrinth.fsm.States
import net.abesto.labyrinth.fsm.States._
import net.abesto.labyrinth.fsm.Transitions._
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
      p => p._1 -> p._2.map(_.asInputMapEntry).toMap
    )

  protected def promptInputMap(ps: (InputMapKey, InputMapValue)*): Map[InputMapKey, InputMapValue] = ps.toMap ++ (
    ('a'.to('z') ++ 'A'.to('Z') ++ Seq(' ', '-') ++ '0'.to('9')).map(c => ks(c) ->
      e(PromptInputEvent((s, cp) => (s.take(cp) + c + s.drop(cp), cp + 1)))
    ).toMap ++ Map[InputMapKey, InputMapValue](
      backspace -> PromptInputEvent((s, cp) => (s.take(cp - 1) + s.drop(cp), cp - 1)),
      leftArrow -> PromptInputEvent((s, cp) => (s, cp - 1)),
      rightArrow -> PromptInputEvent((s, cp) => (s, cp + 1)),
      delete -> PromptInputEvent((s, cp) => (s.take(cp) + s.drop(cp + 1), cp))
    ))

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
    )
  )

  val inputMap: Map[State, Map[Char, (World) => Event]] =
    (rawInputMap ++ editorActionsInputMap).mapValues(_.flatMap(p => p._1.map(_ -> p._2)))

  protected def dispatchEvent(e: Event): (World) => Unit =
    (w: World) => w.getSystem(classOf[EventSystem]).dispatch(e)

  def actionMap(world: World, inputMap: InputMap): Map[InputMapValue, (World) => Unit] =
    inputMap.values.map(v => v -> dispatchEvent(v(world))).toMap
}
