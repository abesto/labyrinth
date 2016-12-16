package net.abesto.labyrinth.render

import asciiPanel.{AsciiFont, AsciiPanel}
import com.badlogic.ashley.core.{ComponentMapper, Engine, Family}
import net.abesto.labyrinth.components.PositionComponent
import net.abesto.labyrinth.map.{MapComponent, MapTile}

case class AsciiPanelRenderer(e: Engine, width: Int, height: Int) extends Renderer {
  val panel = new AsciiPanel(width, height, AsciiFont.DRAKE_10x10)
  val mm = ComponentMapper.getFor(classOf[MapComponent])
  val pm = ComponentMapper.getFor(classOf[PositionComponent])

  def tileToChar(tile: MapTile) = '.'

  def renderMap(): Unit = {
    val mapEntities = e.getEntitiesFor(Family.all(classOf[MapComponent]).get())
    assert(mapEntities.size() == 1)
    val m = mm.get(mapEntities.get(0))
    m.tiles.foreach(_.foreach(t => panel.write(tileToChar(t), t.x, t.y)))
  }

  def renderPlayer(): Unit = {
    val playerEntity = e.getEntitiesFor(Family.all(classOf[PositionComponent]).get()).get(0)
    val position = pm.get(playerEntity)
    panel.write('@', position.x, position.y)
  }

  def render(): Unit = {
    renderMap()
    renderPlayer()
    panel.repaint()
  }
}
