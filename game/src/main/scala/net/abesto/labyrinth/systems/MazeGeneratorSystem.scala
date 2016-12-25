package net.abesto.labyrinth.systems

import com.artemis.{BaseSystem, ComponentMapper}
import com.artemis.managers.TagManager
import net.abesto.labyrinth.Constants
import net.abesto.labyrinth.components.{MazeComponent, PositionComponent}
import net.abesto.labyrinth.events.GenerateMazeEvent
import net.abesto.labyrinth.maze._
import net.mostlyoriginal.api.event.common.Subscribe
import squidpony.squidgrid.mapping.DungeonUtility

class MazeGeneratorSystem extends BaseSystem {
  var tagManager: TagManager = _
  var positionMapper: ComponentMapper[PositionComponent] = _
  var mazeMapper: ComponentMapper[MazeComponent] = _

  var shouldGenerate = false

  @Subscribe
  def generate(e: GenerateMazeEvent): Unit = {
    shouldGenerate = true
  }

  override def checkProcessing(): Boolean = shouldGenerate

  override def processSystem(): Unit = {
    val builder = MazeBuilder.random().hashesToLines().smoothFloor()
    val startPos = new DungeonUtility().randomFloor(builder.get.chars)

    val playerEntityId = tagManager.getEntityId(Constants.Tags.player)
    positionMapper.get(playerEntityId).coord = startPos

    val mazeEntityId = tagManager.getEntityId(Constants.Tags.maze)
    mazeMapper.get(mazeEntityId).maze = builder.roughFloor().get

    shouldGenerate = false
  }
}
