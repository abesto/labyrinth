package net.abesto.labyrinth.ui

import java.awt._
import java.io._
import java.lang.reflect._
import javax.swing._


// Based heavily on http://www.javaspecialists.eu/archive/Issue196.html "Uncaught AWT Exceptions in Java 7"
class SwingExceptionHandler(parent: JFrame) extends Thread.UncaughtExceptionHandler {
  def uncaughtException(t: Thread, e: Throwable) {
    if (SwingUtilities.isEventDispatchThread) {
      showMessage(t, e)
    }
    else {
      try {
        SwingUtilities.invokeAndWait(() => {
          showMessage(t, e)
        })
      }
      catch {
        case ie: InterruptedException =>
          Thread.currentThread.interrupt()
        case ite: InvocationTargetException =>
          // not much more we can do here except log the exception
          ite.getCause.printStackTrace()
      }
    }
  }

  private def generateStackTrace(e: Throwable): String = {
    val writer: StringWriter = new StringWriter
    val pw: PrintWriter = new PrintWriter(writer)
    e.printStackTrace(pw)
    pw.close()
    writer.toString
  }

  private def showMessage(t: Thread, e: Throwable) {
    val stackTrace: String = generateStackTrace(e)
    // show an error dialog
    JOptionPane.showMessageDialog(parent, stackTrace, "Exception Occurred in " + t, JOptionPane.ERROR_MESSAGE)
    parent.dispose()
  }
}