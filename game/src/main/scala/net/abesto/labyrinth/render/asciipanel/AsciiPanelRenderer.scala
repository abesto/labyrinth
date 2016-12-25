package net.abesto.labyrinth.render.asciipanel

import java.awt.Color

import asciiPanel.{AsciiFont, AsciiPanel}
import com.artemis.managers.TagManager
import com.artemis.{Aspect, ComponentMapper}
import net.abesto.labyrinth.components.{LayerComponent, MazeComponent, PositionComponent, TileComponent}
import net.abesto.labyrinth.events.{HidePopupEvent, MessageEvent, ShowPopupEvent}
import net.abesto.labyrinth.maze.{Maze, MazeTile}
import net.abesto.labyrinth.render.Renderer
import net.abesto.labyrinth.{Constants, Tiles}
import net.mostlyoriginal.api.event.common.{EventSystem, Subscribe}
import squidpony.squidmath.Coord

import scala.util.Random

case class AsciiPanelRenderer() extends Renderer {
  var tagManager: TagManager = _
  var mazeMapper: ComponentMapper[MazeComponent] = _
  var positionMapper: ComponentMapper[PositionComponent] = _
  var layerMapper: ComponentMapper[LayerComponent] = _
  var tileMapper: ComponentMapper[TileComponent] = _

  val panel = new AsciiPanel(
    Constants.mazeWidth,
    Constants.mazeHeight + Constants.messageAreaHeight,
    AsciiFont.TALRYTH_15_15)

  lazy val mazeFrame: AsciiPanelMazeFrame = {
    val o = new AsciiPanelMazeFrame(world, panel,
      Coord.get(0, Constants.messageAreaHeight),
      Coord.get(Constants.mazeWidth, Constants.mazeHeight))
    world.inject(o)
    o
  }

  lazy val messageAreaFrame: AsciiPanelMessageAreaFrame = {
    val o = new AsciiPanelMessageAreaFrame(panel,
      Coord.get(0, 0),
      Coord.get(Constants.fullWidth, Constants.messageAreaHeight))
    world.getSystem(classOf[EventSystem]).registerEvents(o)
    o
  }

  lazy val popup: AsciiPanelPopup = {
    val o = new AsciiPanelPopup(panel)
    world.getSystem(classOf[EventSystem]).registerEvents(o)
    o
  }

  override def processSystem(): Unit = {
    mazeFrame.render()
    messageAreaFrame.render()
    popup.render()

    panel.repaint()
  }
}
