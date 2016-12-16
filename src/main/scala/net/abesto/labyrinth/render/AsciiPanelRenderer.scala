package net.abesto.labyrinth.render

import java.awt.Color

import asciiPanel.{AsciiFont, AsciiPanel}
import com.badlogic.ashley.core.{ComponentMapper, Engine, Family}
import net.abesto.labyrinth.EngineAccessors
import net.abesto.labyrinth.components.PositionComponent
import net.abesto.labyrinth.map.{FloorTile, MapComponent, MapTile, WallTile}

case class AsciiPanelRenderer(width: Int, height: Int) extends Renderer {
  val panel = new AsciiPanel(width, height, AsciiFont.DRAKE_10x10)
  val mm = ComponentMapper.getFor(classOf[MapComponent])
  val pm = ComponentMapper.getFor(classOf[PositionComponent])

  def tileToChar(tile: MapTile) = tile match {
    case t: FloorTile => '.'
    case t: WallTile => '#'
  }

  def renderMap(): Unit = {
    val m = EngineAccessors.map(getEngine)
    assert(m.width == width && m.height == height)
    m.tiles.foreach(_.foreach(t => panel.write(
      tileToChar(t), t.x, t.y,
      new Color((255 * t.visibility).toInt, (255 * t.visibility).toInt, (255 * t.visibility).toInt)
    )))
  }

  def renderPlayer(): Unit = {
    val playerEntity = getEngine.getEntitiesFor(Family.all(classOf[PositionComponent]).get()).get(0)
    val position = pm.get(playerEntity)
    panel.write('@', position.x, position.y)
  }

  override def update(deltaTime: Float): Unit = {
    renderMap()
    renderPlayer()
    panel.repaint()
  }
}
