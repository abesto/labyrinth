package net.abesto.labyrinth

import java.awt.event.ActionEvent
import javax.swing.{AbstractAction, JFrame, KeyStroke}

import com.artemis.World
import net.abesto.labyrinth.render.Renderer

class ApplicationMain(world: World, renderer: Renderer) extends JFrame {
  def setup(): Unit = {
    setupLayout()
    setupActions()
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    setVisible(true)
  }

  def setupLayout(): Unit = {
    add(renderer.panel)
    pack()
  }

  def setupActions(): Unit = {
    val rootPaneActionMap = getRootPane.getActionMap
    InputMap.actionMap.foreach {
      case (actionName, action) =>
        rootPaneActionMap.put(actionName, new AbstractAction() {
          override def actionPerformed(e: ActionEvent): Unit = {
            action(world)
            world.setDelta(1)
            world.process()
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

