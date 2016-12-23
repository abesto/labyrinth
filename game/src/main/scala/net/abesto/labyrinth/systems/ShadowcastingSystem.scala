package net.abesto.labyrinth.systems

import com.badlogic.ashley.core.{ComponentMapper, EntitySystem}
import net.abesto.labyrinth.{Constants, EngineAccessors}
import net.abesto.labyrinth.components.PositionComponent
import squidpony.squidgrid.FOV

class ShadowcastingSystem extends EntitySystem {
  val pm = ComponentMapper.getFor(classOf[PositionComponent])

  override def update(deltaTime: Float): Unit = {
    val player = EngineAccessors.player(getEngine)
    val playerPosition = pm.get(player)
    val maze = EngineAccessors.map(getEngine).maze
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
