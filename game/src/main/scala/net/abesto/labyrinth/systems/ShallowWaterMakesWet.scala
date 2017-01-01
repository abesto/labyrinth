package net.abesto.labyrinth.systems

import com.artemis.ComponentMapper
import com.artemis.managers.TagManager
import net.abesto.labyrinth.Constants
import net.abesto.labyrinth.components.{MazeComponent, PositionComponent, WetComponent}
import net.abesto.labyrinth.events.{HasWalkedEvent, MessageEvent}
import net.abesto.labyrinth.fsm.InStates
import net.abesto.labyrinth.fsm.States.GameMazeState
import net.abesto.labyrinth.macros.{DeferredEventHandlerSystem, SubscribeDeferred}
import net.abesto.labyrinth.maze.ShallowWaterTile
import net.mostlyoriginal.api.event.common.EventSystem

@DeferredEventHandlerSystem
@InStates(Array(classOf[GameMazeState]))
class ShallowWaterMakesWet extends InstrumentedSystem {
  var eventSystem: EventSystem = _
  var tagManager: TagManager = _
  var positionMapper: ComponentMapper[PositionComponent] = _
  var wetMapper: ComponentMapper[WetComponent] = _
  var mazeMapper: ComponentMapper[MazeComponent] = _

  @SubscribeDeferred
  def processHasWalked(e: HasWalkedEvent) {
    val mazeEntityId = tagManager.getEntityId(Constants.Tags.maze)
    val maze = mazeMapper.get(mazeEntityId).maze
    val position = positionMapper.get(e.entityId)
    val tile = maze.tile(position)
    if (tile.isInstanceOf[ShallowWaterTile] && !wetMapper.has(e.entityId)) {
      wetMapper.create(e.entityId)
      // The following should be handled by a separate observation system
      eventSystem.dispatch(MessageEvent("You wade into the shallow water. Your clothes are soaked!"))
    }
  }
}
