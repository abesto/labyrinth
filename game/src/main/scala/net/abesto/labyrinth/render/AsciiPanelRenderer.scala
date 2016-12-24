package net.abesto.labyrinth.render

import asciiPanel.{AsciiFont, AsciiPanel}
import com.badlogic.ashley.core.{ComponentMapper, Family}
import net.abesto.labyrinth.components.PositionComponent
import net.abesto.labyrinth.maze.MazeComponent
import net.abesto.labyrinth.{Constants, EngineAccessors, Tiles}

case class AsciiPanelRenderer() extends Renderer {
  val panel = new AsciiPanel(Constants.width, Constants.height, AsciiFont.TALRYTH_15_15)
  protected val mm: ComponentMapper[MazeComponent] = ComponentMapper.getFor(classOf[MazeComponent])
  protected val pm: ComponentMapper[PositionComponent] = ComponentMapper.getFor(classOf[PositionComponent])

  def renderMap(): Unit = {
    val m = EngineAccessors.maze(getEngine).maze
    m.translate(Tiles.dwarfFortress)
    m.tiles.foreach(_.foreach(t => panel.write(
      t.char.character, t.x, t.y,
      t.foregroundColorWithShadow,
      t.char.backgroundColor
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
