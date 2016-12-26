package net.abesto.labyrinth

import java.awt.event.KeyEvent
import javax.swing.KeyStroke

import com.artemis.World
import com.artemis.managers.TagManager
import enumeratum._
import net.abesto.labyrinth.events.{HidePopupEvent, TryWalkingEvent}
import net.mostlyoriginal.api.event.common.{Event, EventSystem}
import squidpony.squidmath.Coord

import scala.language.implicitConversions

object InputMap {
  sealed trait Action extends EnumEntry

  object Action extends Enum[Action] {
    val values = findValues

    // Movement
    case object West extends Action
    case object East extends Action
    case object North extends Action
    case object South extends Action

    // Close an open popup
    case object ClosePopup extends Action
  }

  protected val leftArrow: KeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.VK_UNDEFINED)
  protected val rightArrow: KeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.VK_UNDEFINED)
  protected val upArrow: KeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.VK_UNDEFINED)
  protected val downArrow: KeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.VK_UNDEFINED)
  protected val space: KeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, KeyEvent.VK_UNDEFINED)

  implicit def charToKeyStroke(c: Char): KeyStroke = KeyStroke.getKeyStroke(c)
  implicit def charToKeyStrokeInActionTuple(t: (Char, Action)): (KeyStroke, Action) =
    (t._1, t._2)

  protected def walk(x: Int, y: Int): (World) => Unit =
    (w: World) => w.getSystem(classOf[EventSystem]).dispatch(
      TryWalkingEvent(
        Coord.get(x, y),
        w.getSystem(classOf[TagManager]).getEntityId(Constants.Tags.player)
      )
    )

  protected def dispatchEvent(e: Event): (World) => Unit =
    (w: World) => w.getSystem(classOf[EventSystem]).dispatch(e)

  val actionMap: Map[Action, (World) => Unit] = Map(
    Action.West -> walk(-1, 0),
    Action.East -> walk(1, 0),
    Action.North -> walk(0, -1),
    Action.South -> walk(0, 1),

    Action.ClosePopup -> dispatchEvent(new HidePopupEvent())
  )

  val mainInputMap: Map[KeyStroke, Action] = Map[KeyStroke, Action](
    leftArrow -> Action.West,
    'h' -> Action.West,

    rightArrow -> Action.East,
    'l' -> Action.East,

    upArrow -> Action.North,
    'k' -> Action.North,

    downArrow -> Action.South,
    'j' -> Action.South
  )

  val popupInputMap: Map[KeyStroke, Action] = Map(
    space -> Action.ClosePopup
  )
}
