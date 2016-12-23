package net.abesto.labyrinth.systems

import com.badlogic.ashley.core.EntitySystem
import net.abesto.labyrinth.EngineAccessors
import net.abesto.labyrinth.components.PositionComponent
import net.abesto.labyrinth.maze._
import squidpony.squidgrid.mapping.DungeonUtility

class MazeGeneratorSystem extends EntitySystem {
  var shouldGenerate = false

  def generate(): Unit = {
    shouldGenerate = true
  }

  override def update(deltaTime: Float): Unit = {
    if (shouldGenerate) {
      val mapEntity = EngineAccessors.mapEntity(getEngine)
      val builder = MazeBuilder.random().hashesToLines().smoothFloor()

      val startPos = new DungeonUtility().randomFloor(builder.get.chars)
      EngineAccessors.player(getEngine).add(PositionComponent(startPos))

      val mapComponent = MazeComponent(builder.roughFloor().get)
      mapEntity.add(mapComponent)
      shouldGenerate = false
    }
  }
}
