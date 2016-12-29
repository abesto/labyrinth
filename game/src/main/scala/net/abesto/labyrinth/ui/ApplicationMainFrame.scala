package net.abesto.labyrinth.ui

import java.awt.event.ActionEvent
import javax.swing.{AbstractAction, JFrame, SwingUtilities}

import com.artemis.World
import net.abesto.labyrinth.events.{ActivateInputMapEvent, DeactivateInputMapEvent}
import net.abesto.labyrinth.render.Renderer
import net.abesto.labyrinth.ui.InputMap.InputMap
import net.mostlyoriginal.api.event.common.Subscribe

class ApplicationMainFrame(world: World, renderer: Renderer) extends JFrame {
  var inputMapStack: List[InputMap] = List.empty

  def setup(): Unit = {
    Thread.setDefaultUncaughtExceptionHandler(new SwingExceptionHandler(this))
    SwingUtilities.invokeLater(() => {
      setupLayout()
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
      setVisible(true)
    })
  }

  def setupLayout(): Unit = {
    add(renderer.panel)
    pack()
  }

  def setupActions(inputMap: InputMap): Unit = {
    val rootPaneActionMap = getRootPane.getActionMap
    rootPaneActionMap.clear()
    InputMap.actionMap(world, inputMap).foreach {
      case (event, action) =>
        rootPaneActionMap.put(event, new AbstractAction() {
          override def actionPerformed(e: ActionEvent): Unit = {
            action(world)
            world.setDelta(1)
            world.process()
          }
        })
    }
  }

  def activateInputMap(inputMap: InputMap): Unit = {
    val rootPaneInputMap = getRootPane.getInputMap
    rootPaneInputMap.clear()
    inputMap.foreach {
      case (keystrokes, actionName) => keystrokes.foreach(keystroke => rootPaneInputMap.put(keystroke, actionName))
    }
    setupActions(inputMap)
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

