package net.abesto.labyrinth

import java.awt.event.KeyEvent
import javax.swing.KeyStroke

import com.badlogic.ashley.core.{Engine}
import net.abesto.labyrinth.signals.{MoveData, Signals}

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

  def walk(x: Int, y: Int): (Engine) => Unit =
    (e: Engine) => Signals.tryWalking.dispatch(MoveData(x, y, EngineAccessors.player(e)))

  val actionMap = Map(
    Actions.left -> walk(-1, 0),
    Actions.right -> walk(1, 0),
    Actions.up -> walk(0, -1),
    Actions.down -> walk(0, 1)
  )
}
