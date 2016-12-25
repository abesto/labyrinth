package net.abesto.labyrinth.render.asciipanel

import asciiPanel.AsciiPanel
import com.badlogic.ashley.core.{ComponentMapper, Engine}
import net.abesto.labyrinth.{EngineAccessors, Tiles}
import net.abesto.labyrinth.components.{LayerComponent, PositionComponent, TileComponent}
import net.abesto.labyrinth.maze.{Maze, MazeTile}
import squidpony.squidmath.Coord

import scala.util.Random

class AsciiPanelMazeFrame(engine: Engine, panel: AsciiPanel, topLeft: Coord, size: Coord) extends AsciiPanelFrame(panel, topLeft, size) {
  protected val pm: ComponentMapper[PositionComponent] = ComponentMapper.getFor(classOf)
  protected val tm: ComponentMapper[TileComponent] = ComponentMapper.getFor(classOf)

  def render(): Unit = {
    val m = EngineAccessors.maze(engine).maze
    m.translate(Tiles.dwarfFortress)
    m.tiles.foreach(_.foreach(t => write(
      coalesceChar(m, t),
      t.x, t.y,
      t.foregroundColorWithShadow,
      t.char.backgroundColor
    )))
  }

  def coalesceChar(m: Maze, t: MazeTile): Char =
    0.until(LayerComponent.Layers.maxId).map(LayerComponent.Layers(_)).flatMap(
    l => {
      val entities = EngineAccessors.entitiesAt(engine, t.coord, l).toArray
      if (entities.isEmpty) {
        None
      } else {
        Some(
          tm.get(entities(Random.nextInt(entities.length))).kind
        )
      }
    }
  ).headOption.map(m.tileset.toChar(_)).getOrElse(t.char.character)
}
