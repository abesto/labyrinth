package net.abesto.labyrinth.render

import java.awt.Color

import asciiPanel.{AsciiFont, AsciiPanel}
import com.badlogic.ashley.core.{ComponentMapper, Engine, Family}
import net.abesto.labyrinth.{Constants, EngineAccessors}
import net.abesto.labyrinth.components.PositionComponent
import net.abesto.labyrinth.map.{FloorTile, MapComponent, MapTile, WallTile}

case class AsciiPanelRenderer() extends Renderer {
  val panel = new AsciiPanel(Constants.width, Constants.height, AsciiFont.TALRYTH_15_15)
  val mm = ComponentMapper.getFor(classOf[MapComponent])
  val pm = ComponentMapper.getFor(classOf[PositionComponent])

  def renderMap(): Unit = {
    val m = EngineAccessors.map(getEngine)
    m.tiles.foreach(_.foreach(t => panel.write(
      t.char, t.x, t.y,
      new Color((255 * t.visibility).toInt, (255 * t.visibility).toInt, (255 * t.visibility).toInt)
    )))
  }

  def renderPlayer(): Unit = {
    val playerEntity = getEngine.getEntitiesFor(Family.all(classOf[PositionComponent]).get()).get(0)
    val position = pm.get(playerEntity)
    panel.write(2.toChar, position.x, position.y)
  }

  override def update(deltaTime: Float): Unit = {
    renderMap()
    renderPlayer()
    panel.repaint()
  }
}
