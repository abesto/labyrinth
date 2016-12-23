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
      val mapComponent = MazeComponent(
        new MazeBuilder().generateMaze().hashesToLines().roughFloor().unicodeToAscii().get
      )
      mapEntity.add(mapComponent)

      val startPos = new DungeonUtility().randomFloor(mapComponent.chars)
      EngineAccessors.player(getEngine).add(PositionComponent(startPos))

      shouldGenerate = false
    }
  }
}
