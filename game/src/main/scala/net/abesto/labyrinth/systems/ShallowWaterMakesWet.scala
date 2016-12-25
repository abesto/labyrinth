package net.abesto.labyrinth.systems

import com.artemis.managers.TagManager
import com.artemis.{BaseSystem, ComponentMapper}
import net.abesto.labyrinth.Constants
import net.abesto.labyrinth.components.{MazeComponent, PositionComponent, WetComponent}
import net.abesto.labyrinth.events.{HasWalkedEvent, MessageEvent}
import net.abesto.labyrinth.maze.ShallowWaterTile
import net.mostlyoriginal.api.event.common.{EventSystem, Subscribe}

import scala.collection.immutable.Queue

class ShallowWaterMakesWet extends BaseSystem {
  var eventSystem: EventSystem = _
  var tagManager: TagManager = _
  var positionMapper: ComponentMapper[PositionComponent] = _
  var wetMapper: ComponentMapper[WetComponent] = _
  var mazeMapper: ComponentMapper[MazeComponent] = _

  var hasWalkedInbox: Queue[HasWalkedEvent] = Queue()

  @Subscribe
  def enqueueHasWalked(e: HasWalkedEvent): Unit = {
    hasWalkedInbox :+= e
  }

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

  override def processSystem(): Unit = {
    hasWalkedInbox.foreach(processHasWalked)
    hasWalkedInbox = Queue()
  }
}
