package net.abesto.labyrinth.render.asciipanel

import java.awt.Color

import asciiPanel.AsciiPanel
import com.artemis.ComponentMapper
import com.artemis.managers.TagManager
import net.abesto.labyrinth.Tiles
import net.abesto.labyrinth.components._
import net.abesto.labyrinth.fsm.InStates
import net.abesto.labyrinth.fsm.States.{EditorState, GameState}
import net.abesto.labyrinth.maze.{Maze, MazeTile}
import net.abesto.labyrinth.systems.Helpers
import squidpony.squidmath.Coord

import scala.util.Random

@InStates(Array(classOf[GameState], classOf[EditorState]))
class MazeFrame(topLeftX: Int, topLeftY: Int, width: Int, height: Int) extends Frame(topLeftX, topLeftY, width, height) {
  var tagManager: TagManager = _
  var positionMapper: ComponentMapper[PositionComponent] = _
  var layerMapper: ComponentMapper[LayerComponent] = _
  var tileMapper: ComponentMapper[TileComponent] = _
  var highlightMapper: ComponentMapper[MazeHighlightComponent] = _
  var helpers: Helpers = _

  def render(): Unit = {
    val maze = helpers.maze
    val highlight = helpers.highlight.highlight _
    def backgroundColor(c: Coord): Color = highlight(c).getOrElse(AsciiPanel.black)

    maze.translate(Tiles.dwarfFortress)
    maze.tiles.foreach(_.foreach(t => write(
      coalesceChar(maze, t),
      t.x, t.y,
      t.foregroundColorWithShadow,
      backgroundColor(t.coord)
    )))
  }

  def coalesceChar(m: Maze, t: MazeTile): Char =
    LayerComponent.Layer.values.toStream.flatMap(
      l => {
        val entityIds = helpers.entityIdsAtPosition(l, t.coord)
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
