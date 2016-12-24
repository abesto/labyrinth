package net.abesto.labyrinth.render.asciipanel

import asciiPanel.{AsciiFont, AsciiPanel}
import com.badlogic.ashley.core.ComponentMapper
import net.abesto.labyrinth.maze.MazeComponent
import net.abesto.labyrinth.render.Renderer
import net.abesto.labyrinth.{Constants, EngineAccessors}
import squidpony.squidmath.Coord

case class AsciiPanelRenderer() extends Renderer {
  val panel = new AsciiPanel(
    Constants.mazeWidth,
    Constants.mazeHeight + Constants.messageAreaHeight,
    AsciiFont.TALRYTH_15_15)
  protected val mm: ComponentMapper[MazeComponent] = ComponentMapper.getFor(classOf[MazeComponent])

  val mazeFrame = new AsciiPanelMazeFrame(panel,
    Coord.get(0, Constants.messageAreaHeight),
    Coord.get(Constants.mazeWidth, Constants.mazeHeight))

  val messageAreaFrame = new AsciiPanelMessageAreaFrame(panel,
    Coord.get(0, 0),
    Coord.get(Constants.fullWidth, Constants.messageAreaHeight)
  )

  val popup = new AsciiPanelPopup(panel)

  override def update(deltaTime: Float): Unit = {
    mazeFrame.render(
      EngineAccessors.maze(getEngine).maze,
      EngineAccessors.player(getEngine)
    )
    messageAreaFrame.render()
    popup.render()

    panel.repaint()
  }
}
