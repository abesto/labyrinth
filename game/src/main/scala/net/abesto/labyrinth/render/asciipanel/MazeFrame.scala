package net.abesto.labyrinth.render.asciipanel

import java.awt.Color

import asciiPanel.AsciiPanel
import com.artemis.ComponentMapper
import com.artemis.managers.TagManager
import net.abesto.labyrinth.components._
import net.abesto.labyrinth.fsm.InState
import net.abesto.labyrinth.fsm.States.GameState
import net.abesto.labyrinth.maze.{Maze, MazeTile}
import net.abesto.labyrinth.systems.Helpers
import net.abesto.labyrinth.{Constants, Tiles}
import squidpony.squidmath.Coord

import scala.util.Random

@InState(classOf[GameState])
class MazeFrame(topLeftX: Int, topLeftY: Int, width: Int, height: Int) extends Frame(topLeftX, topLeftY, width, height) {
  var tagManager: TagManager = _
  var mazeMapper: ComponentMapper[MazeComponent] = _
  var positionMapper: ComponentMapper[PositionComponent] = _
  var layerMapper: ComponentMapper[LayerComponent] = _
  var tileMapper: ComponentMapper[TileComponent] = _
  var spellInputMapper: ComponentMapper[SpellInputComponent] = _
  var helpers: Helpers = _

  def render(): Unit = {
    val mazeEntityId = tagManager.getEntityId(Constants.Tags.maze)
    val maze = mazeMapper.get(mazeEntityId).maze
    val spell = spellInputMapper.get(tagManager.getEntityId(Constants.Tags.spellInput)).spell
    val highlightSpellTarget: Map[Coord, (MazeTile) => Color] = spell.map(_.target.affectedTiles(world)).getOrElse(Seq.empty).map(
      c => c -> ((_: MazeTile) => AsciiPanel.green)
    ).toMap.withDefaultValue((t: MazeTile) => t.char.backgroundColor)
    maze.translate(Tiles.dwarfFortress)
    maze.tiles.foreach(_.foreach(t => write(
      coalesceChar(maze, t),
      t.x, t.y,
      t.foregroundColorWithShadow,
      highlightSpellTarget(t.coord)(t)
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
