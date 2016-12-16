package net.abesto.labyrinth

import java.awt.event.ActionEvent
import javax.swing.{AbstractAction, JFrame, KeyStroke}

import net.abesto.labyrinth.actions.Action
import net.abesto.labyrinth.render.Renderer

class ApplicationMain(renderer: Renderer) extends JFrame {
  def setup(actionHandler: (Action => Unit)): Unit = {
    setupLayout()
    setupActions(actionHandler)
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    setVisible(true)
  }

  def setupLayout(): Unit = {
    add(renderer.panel)
    pack()
  }

  def setupActions(handleAction: (Action => Unit)): Unit = {
    val rootPaneActionMap = getRootPane.getActionMap
    InputMap.actionMap.foreach {
      case (actionName, action) =>
        rootPaneActionMap.put(actionName, new AbstractAction() {
          override def actionPerformed(e: ActionEvent): Unit = {
            handleAction(action)
          }
        })
    }
    activateInputMap(InputMap.mainInputMap)
  }

  def activateInputMap(inputMap: Map[KeyStroke, String]): Unit = {
    val rootPaneInputMap = getRootPane.getInputMap
    rootPaneInputMap.clear()
    inputMap.foreach {
      case (keystroke, actionName) => rootPaneInputMap.put(keystroke, actionName)
    }
  }
}

