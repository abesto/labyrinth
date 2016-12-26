package net.abesto.labyrinth.systems

import com.artemis.ComponentMapper
import com.artemis.managers.TagManager
import net.abesto.labyrinth.Constants
import net.abesto.labyrinth.components.{MazeComponent, PositionComponent}
import net.abesto.labyrinth.events.GenerateMazeEvent
import net.abesto.labyrinth.macros.{SubscribeDeferred, SubscribeDeferredContainer}
import net.abesto.labyrinth.maze._
import squidpony.squidgrid.mapping.DungeonUtility

@SubscribeDeferredContainer
class MazeGeneratorSystem extends EventHandlerSystem {
  var tagManager: TagManager = _
  var positionMapper: ComponentMapper[PositionComponent] = _
  var mazeMapper: ComponentMapper[MazeComponent] = _

  @SubscribeDeferred
  def generate(e: GenerateMazeEvent): Unit = {
    val builder = MazeBuilder.random().hashesToLines().smoothFloor()
    val startPos = new DungeonUtility().randomFloor(builder.get.chars)

    val playerEntityId = tagManager.getEntityId(Constants.Tags.player)
    positionMapper.get(playerEntityId).coord = startPos

    val mazeEntityId = tagManager.getEntityId(Constants.Tags.maze)
    mazeMapper.get(mazeEntityId).maze = builder.roughFloor().get
  }
}
