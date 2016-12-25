package net.abesto.labyrinth.render.asciipanel

import asciiPanel.{AsciiFont, AsciiPanel}
import com.badlogic.ashley.core.{ComponentMapper, Engine}
import net.abesto.labyrinth.Constants
import net.abesto.labyrinth.maze.MazeComponent
import net.abesto.labyrinth.render.Renderer
import squidpony.squidmath.Coord

case class AsciiPanelRenderer() extends Renderer {
  val panel = new AsciiPanel(
    Constants.mazeWidth,
    Constants.mazeHeight + Constants.messageAreaHeight,
    AsciiFont.TALRYTH_15_15)
  protected val mm: ComponentMapper[MazeComponent] = ComponentMapper.getFor(classOf[MazeComponent])

  lazy val mazeFrame = new AsciiPanelMazeFrame(getEngine, panel,
    Coord.get(0, Constants.messageAreaHeight),
    Coord.get(Constants.mazeWidth, Constants.mazeHeight))

  lazy val messageAreaFrame = new AsciiPanelMessageAreaFrame(panel,
    Coord.get(0, 0),
    Coord.get(Constants.fullWidth, Constants.messageAreaHeight)
  )

  lazy val popup = new AsciiPanelPopup(panel)

  override def update(deltaTime: Float): Unit = {
    mazeFrame.render()
    messageAreaFrame.render()
    popup.render()

    panel.repaint()
  }
}
