package net.abesto.labyrinth.render.asciipanel

import asciiPanel.AsciiPanel
import com.badlogic.ashley.core.{ComponentMapper, Entity}
import net.abesto.labyrinth.Tiles
import net.abesto.labyrinth.components.PositionComponent
import net.abesto.labyrinth.maze.Maze
import squidpony.squidmath.Coord

class AsciiPanelMazeFrame(panel: AsciiPanel, topLeft: Coord, size: Coord) extends AsciiPanelFrame(panel, topLeft, size) {
  protected val pm: ComponentMapper[PositionComponent] = ComponentMapper.getFor(classOf[PositionComponent])

  def render(m: Maze, p: Entity): Unit = {
    renderMaze(m)
    renderPlayer(m, p)
  }

  def renderMaze(m: Maze): Unit = {
    m.translate(Tiles.dwarfFortress)
    m.tiles.foreach(_.foreach(t => write(
      t.char.character, t.x, t.y,
      t.foregroundColorWithShadow,
      t.char.backgroundColor
    )))
  }

  def renderPlayer(m: Maze, playerEntity: Entity): Unit = {
    val position = pm.get(playerEntity)
    write(Tiles.dwarfFortress.toChar(Tiles.Kind.Player),
      position.x, position.y,
      AsciiPanel.white,
      m.tile(position).char.backgroundColor
    )
  }
}
