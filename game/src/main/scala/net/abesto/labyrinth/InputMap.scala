package net.abesto.labyrinth

import java.awt.event.KeyEvent
import javax.swing.KeyStroke

import com.artemis.World
import com.artemis.managers.TagManager
import net.abesto.labyrinth.events.TryWalkingEvent
import net.mostlyoriginal.api.event.common.EventSystem
import squidpony.squidmath.Coord

object InputMap {
  object Actions {
    val left = "left"
    val right = "right"
    val up = "up"
    val down = "down"
  }

  val leftArrow = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.VK_UNDEFINED)
  val rightArrow = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.VK_UNDEFINED)
  val upArrow = KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.VK_UNDEFINED)
  val downArrow = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.VK_UNDEFINED)

  implicit def charToKeyStroke(c: Char): KeyStroke = KeyStroke.getKeyStroke(c)
  implicit def charToKeyStrokeInActionTuple(t: (Char, String)): (KeyStroke, String) =
    (t._1, t._2)

  val mainInputMap = Map[KeyStroke, String](
    leftArrow -> Actions.left,
    'h' -> Actions.left,

    rightArrow -> Actions.right,
    'l' -> Actions.right,

    upArrow -> Actions.up,
    'k' -> Actions.up,

    downArrow -> Actions.down,
    'j' -> Actions.down
  )

  def walk(x: Int, y: Int): (World) => Unit =
    (w: World) => w.getSystem(classOf[EventSystem]).dispatch(
      TryWalkingEvent(
        Coord.get(x, y),
        w.getSystem(classOf[TagManager]).getEntityId(Constants.Tags.player)
      )
    )

  val actionMap = Map(
    Actions.left -> walk(-1, 0),
    Actions.right -> walk(1, 0),
    Actions.up -> walk(0, -1),
    Actions.down -> walk(0, 1)
  )
}
