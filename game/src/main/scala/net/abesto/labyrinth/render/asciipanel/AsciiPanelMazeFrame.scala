package net.abesto.labyrinth.render.asciipanel

import asciiPanel.AsciiPanel
import com.artemis.managers.TagManager
import com.artemis.{Aspect, ComponentMapper, World}
import net.abesto.labyrinth.components.{LayerComponent, MazeComponent, PositionComponent, TileComponent}
import net.abesto.labyrinth.maze.{Maze, MazeTile}
import net.abesto.labyrinth.{Constants, Helpers, Tiles}
import squidpony.squidmath.Coord

import scala.util.Random

class AsciiPanelMazeFrame(world: World, panel: AsciiPanel, topLeft: Coord, size: Coord) extends AsciiPanelFrame(panel, topLeft, size) {
  var tagManager: TagManager = _
  var mazeMapper: ComponentMapper[MazeComponent] = _
  var positionMapper: ComponentMapper[PositionComponent] = _
  var layerMapper: ComponentMapper[LayerComponent] = _
  var tileMapper: ComponentMapper[TileComponent] = _

  def render(): Unit = {
    val mazeEntityId = tagManager.getEntityId(Constants.Tags.maze)
    val maze = mazeMapper.get(mazeEntityId).maze
    maze.translate(Tiles.dwarfFortress)
    maze.tiles.foreach(_.foreach(t => write(
      coalesceChar(maze, t),
      t.x, t.y,
      t.foregroundColorWithShadow,
      t.char.backgroundColor
    )))
  }

  def coalesceChar(m: Maze, t: MazeTile): Char =
    LayerComponent.Layer.values.toStream.flatMap(
      l => {
        val entityIds = Helpers.entityIdsOfAspect(world,
          Aspect.all(classOf[PositionComponent], classOf[LayerComponent])
        ).filter(
          id => layerMapper.get(id).layer == l && positionMapper.get(id).coord.equals(t.coord)
        )
        if (entityIds.isEmpty) {
          None
        } else {
          Some(
            tileMapper.get(entityIds(Random.nextInt(entityIds.length))).kind
          )
        }
      }
    ).headOption.map(m.tileset.toChar(_)).getOrElse(t.char.character)
}
