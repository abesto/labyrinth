package net.abesto.labyrinth.ui

import java.awt.event.KeyEvent
import javax.swing.KeyStroke

import com.artemis.World
import com.artemis.managers.TagManager
import net.abesto.labyrinth.Constants
import net.abesto.labyrinth.events._
import net.abesto.labyrinth.fsm.States._
import net.abesto.labyrinth.fsm.Transitions.{HidePopupEvent, SpellInputAbortEvent, SpellInputFinishEvent, SpellInputStartEvent}
import net.mostlyoriginal.api.event.common.{Event, EventSystem}
import squidpony.squidmath.Coord

import scala.collection.immutable.Seq
import scala.language.implicitConversions

object InputMap {
  type InputMapKey = Seq[KeyStroke]
  type InputMapValue = (World => Event)
  type InputMap = Map[InputMapKey, InputMapValue]
  type InputMapEntry = (InputMapKey, InputMapValue)

  protected val leftArrow: KeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.VK_UNDEFINED)
  protected val rightArrow: KeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.VK_UNDEFINED)
  protected val upArrow: KeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.VK_UNDEFINED)
  protected val downArrow: KeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.VK_UNDEFINED)
  protected val space: KeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, KeyEvent.VK_UNDEFINED)
  protected val enter: KeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.VK_UNDEFINED)
  protected val escape: KeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, KeyEvent.VK_UNDEFINED)
  protected val backspace: KeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, KeyEvent.VK_UNDEFINED)
  protected val delete: KeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, KeyEvent.VK_UNDEFINED)

  implicit def k(c: Char): KeyStroke = KeyStroke.getKeyStroke(c)
  implicit def ks(c: Char): InputMapKey = Seq(c)
  implicit def tuC(t: (Char, Event)): InputMapEntry = (ks(t._1), t._2)
  implicit def tuK(t: (KeyStroke, Event)): InputMapEntry = (Seq(t._1), t._2)
  implicit def oneToSeq(k: KeyStroke): InputMapKey = Seq(k)
  implicit def e(evt: Event): InputMapValue = (w: World) => evt

  protected def walk(x: Int, y: Int): (World) => Event =
    (w: World) => TryWalkingEvent(
      Coord.get(x, y),
      w.getSystem(classOf[TagManager]).getEntityId(Constants.Tags.player)
    )

  protected val rawInputMap: Map[Class[_ <: State], InputMap] = Map(
    classOf[GameMazeState] -> Map(
      Seq(leftArrow, k('h')) -> walk(-1, 0),
      Seq(rightArrow, k('l')) -> walk(1, 0),
      Seq(downArrow, k('j')) -> walk(0, 1),
      Seq(upArrow, k('k')) -> walk(0, -1),
      'z' -> new SpellInputStartEvent
    ),
    classOf[GamePopupState] -> Map(
      space -> new HidePopupEvent
    ),
    classOf[GameSpellInputState] -> (
      ('a'.to('z') ++ 'A'.to('Z') ++ Seq(' ')).map(c => ks(c) ->
        e(SpellInputOperationEvent((s, cp) => (s.take(cp) + c + s.drop(cp), cp + 1)))
      ).toMap ++ Map[InputMapKey, InputMapValue](
        enter -> new SpellInputFinishEvent,
        escape -> new SpellInputAbortEvent,
        backspace -> SpellInputOperationEvent((s, cp) => (s.take(cp - 1) + s.drop(cp), cp - 1)),
        leftArrow -> SpellInputOperationEvent((s, cp) => (s, cp - 1)),
        rightArrow -> SpellInputOperationEvent((s, cp) => (s, cp + 1)),
        delete -> SpellInputOperationEvent((s, cp) => (s.take(cp) + s.drop(cp + 1), cp))
      )
    ),
    classOf[MainMenuState] -> Map(
      upArrow -> MainMenuMoveEvent(_ - 1),
      downArrow -> MainMenuMoveEvent(_ + 1),
      enter -> new MainMenuSelectedEvent
    ),
    classOf[EditorState] -> Map(
      upArrow -> EditorMoveMazeCursorEvent(_.add(Coord.get(0, -1))),
      downArrow -> EditorMoveMazeCursorEvent(_.add(Coord.get(0, 1))),
      leftArrow -> EditorMoveMazeCursorEvent(_.add(Coord.get(-1, 0))),
      rightArrow -> EditorMoveMazeCursorEvent(_.add(Coord.get(1, 0))),
      k('g') -> new EditorGenerateMazeEvent
    )
  )

  val inputMap: Map[Class[_ <: State], Map[KeyStroke, (World) => Event]] =
    rawInputMap.mapValues(_.flatMap(p => p._1.map(_ -> p._2)))

  protected def dispatchEvent(e: Event): (World) => Unit =
    (w: World) => w.getSystem(classOf[EventSystem]).dispatch(e)

  def actionMap(world: World, inputMap: InputMap): Map[InputMapValue, (World) => Unit] =
    inputMap.values.map(v => v -> dispatchEvent(v(world))).toMap
}
