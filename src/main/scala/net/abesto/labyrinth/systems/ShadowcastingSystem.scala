package net.abesto.labyrinth.systems

import com.badlogic.ashley.core.{ComponentMapper, EntitySystem}
import net.abesto.labyrinth.EngineAccessors
import net.abesto.labyrinth.components.PositionComponent
import squidpony.squidgrid.FOV

class ShadowcastingSystem extends EntitySystem {
  val pm = ComponentMapper.getFor(classOf[PositionComponent])

  override def update(deltaTime: Float): Unit = {
    val player = EngineAccessors.player(getEngine)
    val playerPosition = pm.get(player)
    val map = EngineAccessors.map(getEngine)
    val fov = new FOV()

    val inputResistances: Array[Array[Double]] = map.tiles.map(_.map(t => if (t.blocksSight) 1.0 else 0.0)).transpose
    val outputResistances = fov.calculateFOV(inputResistances, playerPosition.x, playerPosition.y, 7)
    outputResistances.transpose.zipWithIndex.foreach {
      case (row, y) => row.zipWithIndex.foreach {
        case (visibility, x) => map.tile(x, y).visibility = visibility
      }
    }
  }
}
