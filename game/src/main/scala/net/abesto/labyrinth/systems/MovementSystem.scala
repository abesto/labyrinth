package net.abesto.labyrinth.systems

import com.artemis.managers.TagManager
import com.artemis.{BaseSystem, ComponentMapper}
import net.abesto.labyrinth.Constants
import net.abesto.labyrinth.components.{MazeComponent, PositionComponent}
import net.abesto.labyrinth.events.{HasWalkedEvent, TryWalkingEvent}
import net.abesto.labyrinth.fsm.InStates
import net.abesto.labyrinth.fsm.States.GameMazeState
import net.abesto.labyrinth.macros.{DeferredEventHandlerSystem, SubscribeDeferred}
import net.mostlyoriginal.api.event.common.EventSystem

@InStates(Array(classOf[GameMazeState]))
@DeferredEventHandlerSystem
class MovementSystem extends BaseSystem {
  var eventSystem: EventSystem = _
  var tagManager: TagManager = _
  var positionMapper: ComponentMapper[PositionComponent] = _
  var mazeMapper: ComponentMapper[MazeComponent] = _

  @SubscribeDeferred
  def walk(e: TryWalkingEvent): Unit = {
    val maze = mazeMapper.get(tagManager.getEntityId(Constants.Tags.maze)).maze
    val oldPosition = positionMapper.get(e.entityId).coord
    val newPosition = oldPosition.add(e.vector)
    val withinBounds = newPosition.isWithin(Constants.mazeWidth, Constants.mazeHeight)
    val canWalk = withinBounds && maze.tile(newPosition).canBeStoodOn
    if (canWalk) {
      positionMapper.get(e.entityId).coord = newPosition
      eventSystem.dispatch(HasWalkedEvent(e.entityId))
    }
  }
}
