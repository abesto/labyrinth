package net.abesto.labyrinth.systems

import com.artemis.managers.TagManager
import com.artemis.{BaseSystem, ComponentMapper}
import net.abesto.labyrinth.Constants
import net.abesto.labyrinth.components.{MazeComponent, PositionComponent}
import net.abesto.labyrinth.events.GenerateMazeEvent
import net.abesto.labyrinth.fsm.Transitions.OpenEditorEvent
import net.abesto.labyrinth.macros._
import net.abesto.labyrinth.maze._
import net.mostlyoriginal.api.event.common.EventSystem
import squidpony.squidgrid.mapping.DungeonUtility

@DeferredEventHandlerSystem
class MazeGeneratorSystem extends BaseSystem {
  var tagManager: TagManager = _
  var positionMapper: ComponentMapper[PositionComponent] = _
  var mazeMapper: ComponentMapper[MazeComponent] = _
  var eventSystem: EventSystem = _

  @SubscribeDeferred
  def generate(e: GenerateMazeEvent): Unit = {
    val builder = MazeBuilder.random().hashesToLines().smoothFloor()
    val startPos = new DungeonUtility().randomFloor(builder.get.chars)

    val playerEntityId = tagManager.getEntityId(Constants.Tags.player)
    positionMapper.get(playerEntityId).coord = startPos

    val mazeEntityId = tagManager.getEntityId(Constants.Tags.maze)
    mazeMapper.get(mazeEntityId).maze = builder.roughFloor().get
  }

  @SubscribeDeferred
  def openEditor(e: OpenEditorEvent): Unit = {
    eventSystem.dispatch(new GenerateMazeEvent)
  }
}
