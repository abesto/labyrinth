package net.abesto.labyrinth

import java.awt.event.KeyEvent
import javax.swing.KeyStroke

import net.abesto.labyrinth.actions.{Action, WalkAction}

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

  val actionMap = Map[String, Action](
    Actions.left -> WalkAction(-1, 0),
    Actions.right -> WalkAction(1, 0),
    Actions.up -> WalkAction(0, -1),
    Actions.down -> WalkAction(0, 1)
  )
}
