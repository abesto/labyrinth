package net.abesto.labyrinth.render.asciipanel

import asciiPanel.{AsciiFont, AsciiPanel}
import com.artemis.ComponentMapper
import com.artemis.managers.TagManager
import net.abesto.labyrinth.Constants
import net.abesto.labyrinth.components.{LayerComponent, MazeComponent, PositionComponent, TileComponent}
import net.abesto.labyrinth.render.Renderer
import net.mostlyoriginal.api.event.common.EventSystem
import squidpony.squidmath.Coord

case class AsciiPanelRenderer() extends Renderer {
  var eventSystem: EventSystem = _
  var tagManager: TagManager = _
  var mazeMapper: ComponentMapper[MazeComponent] = _
  var positionMapper: ComponentMapper[PositionComponent] = _
  var layerMapper: ComponentMapper[LayerComponent] = _
  var tileMapper: ComponentMapper[TileComponent] = _

  val panel = new AsciiPanel(
    Constants.fullWidth,
    Constants.fullHeight,
    AsciiFont.TALRYTH_15_15)

  def artemisify[T](o: T): T = {
    world.inject(o)
    eventSystem.registerEvents(o)
    o
  }

  lazy val mazeFrame: AsciiPanelMazeFrame = artemisify(
    new AsciiPanelMazeFrame(world, panel,
      Coord.get(0, Constants.messageAreaHeight),
      Coord.get(Constants.mazeWidth, Constants.mazeHeight))
  )

  lazy val messageAreaFrame: AsciiPanelMessageAreaFrame = artemisify(
    new AsciiPanelMessageAreaFrame(panel,
      Coord.get(0, 0),
      Coord.get(Constants.fullWidth, Constants.messageAreaHeight))
  )

  lazy val popup: AsciiPanelPopup = artemisify(new AsciiPanelPopup(panel))

  lazy val castingPrompt: AsciiPanelSpellInputFrame = artemisify(
    new AsciiPanelSpellInputFrame(panel,
      Coord.get(0, Constants.fullHeight - Constants.castingPromptHeight),
      Coord.get(Constants.fullWidth, Constants.castingPromptHeight))
  )

  override def processSystem(): Unit = {
    mazeFrame.render()
    messageAreaFrame.render()
    popup.render()
    castingPrompt.render()

    panel.repaint()
  }
}
