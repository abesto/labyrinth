package net.abesto.labyrinth

import java.awt.event.ActionEvent
import javax.swing.{AbstractAction, JFrame, KeyStroke}

import com.artemis.World
import net.abesto.labyrinth.InputMap.Action
import net.abesto.labyrinth.events.{ActivateInputMapEvent, DeactivateInputMapEvent}
import net.abesto.labyrinth.render.Renderer
import net.mostlyoriginal.api.event.common.Subscribe

class ApplicationMain(world: World, renderer: Renderer) extends JFrame {
  var inputMapStack: List[Map[KeyStroke, Action]] = List.empty

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
  }

  def activateInputMap(inputMap: Map[KeyStroke, Action]): Unit = {
    val rootPaneInputMap = getRootPane.getInputMap
    rootPaneInputMap.clear()
    inputMap.foreach {
      case (keystroke, actionName) => rootPaneInputMap.put(keystroke, actionName)
    }
  }

  @Subscribe
  def handleActivateInputMap(e: ActivateInputMapEvent): Unit = {
    inputMapStack = e.inputMap +: inputMapStack
    activateInputMap(e.inputMap)
  }

  @Subscribe
  def deactivateInputMap(e: DeactivateInputMapEvent): Unit = {
    assert(inputMapStack.head == e.inputMap)
    inputMapStack = inputMapStack.tail
    activateInputMap(inputMapStack.head)
  }
}

