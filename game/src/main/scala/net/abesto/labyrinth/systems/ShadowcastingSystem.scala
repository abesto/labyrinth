package net.abesto.labyrinth.systems

import com.artemis.managers.TagManager
import com.artemis.{BaseSystem, ComponentMapper}
import net.abesto.labyrinth.Constants
import net.abesto.labyrinth.components.{MazeComponent, PositionComponent}
import net.abesto.labyrinth.fsm.InStates
import net.abesto.labyrinth.fsm.States.GameState
import squidpony.squidgrid.FOV

@InStates(Array(classOf[GameState]))
class ShadowcastingSystem extends BaseSystem {
  var positionMapper: ComponentMapper[PositionComponent] = _
  var mazeMapper: ComponentMapper[MazeComponent] = _
  var tagManager: TagManager = _

  override def processSystem(): Unit = {
    val playerEntityId = tagManager.getEntityId(Constants.Tags.player)
    val playerPosition = positionMapper.get(playerEntityId)
    val mazeEntityId = tagManager.getEntityId(Constants.Tags.maze)
    val maze = mazeMapper.get(mazeEntityId).maze
    val fov = new FOV()

    val inputResistances: Array[Array[Double]] = maze.tiles.map(_.map(t => if (t.blocksSight) 1.0 else 0.0))
    val outputResistances = fov.calculateFOV(inputResistances, playerPosition.x, playerPosition.y, Constants.sightRadius)
    outputResistances.zipWithIndex.foreach {
      case (column, x) => column.zipWithIndex.foreach {
        case (visibility, y) =>
          val tile = maze.tile(x, y)
          tile.seen ||= visibility > 0.1
          tile.visibility = if (tile.seen) {
            math.max(0.1, visibility)
          } else {
            visibility
          }
      }
    }
  }
}
