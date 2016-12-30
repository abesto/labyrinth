package net.abesto.labyrinth.ui

import java.awt.event.{KeyListener, KeyEvent => AwtKeyEvent}
import javax.swing.{JFrame, SwingUtilities}

import com.artemis.World
import net.abesto.labyrinth.events.KeyEvent
import net.abesto.labyrinth.fsm.Transitions.MainMenuQuitEvent
import net.abesto.labyrinth.render.Renderer
import net.mostlyoriginal.api.event.common.{EventSystem, Subscribe}

class ApplicationMainFrame(world: World, renderer: Renderer) extends JFrame {
  var eventSystem: EventSystem = _

  def setup(): Unit = {
    world.inject(this)
    eventSystem.registerEvents(this)
    Thread.setDefaultUncaughtExceptionHandler(new SwingExceptionHandler(this))
    SwingUtilities.invokeLater(() => {
      setupLayout()
      setupKeyListener()
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
      setVisible(true)
    })
  }

  def setupLayout(): Unit = {
    add(renderer.panel)
    pack()
  }

  def dispatchKeyEvent(e: AwtKeyEvent): Unit = {
    world.getSystem(classOf[EventSystem]).dispatch(KeyEvent(e))
    world.setDelta(1)
    world.process()
  }

  def setupKeyListener(): Unit = {
    addKeyListener(new KeyListener {
      override def keyTyped(e: AwtKeyEvent): Unit = dispatchKeyEvent(e)
      override def keyPressed(e: AwtKeyEvent): Unit = dispatchKeyEvent(e)
      override def keyReleased(e: AwtKeyEvent): Unit = dispatchKeyEvent(e)
    })
  }

  @Subscribe
  def quit(e: MainMenuQuitEvent): Unit = this.dispose()
}

