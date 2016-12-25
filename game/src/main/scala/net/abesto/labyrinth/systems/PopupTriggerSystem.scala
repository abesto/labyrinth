package net.abesto.labyrinth.systems

import com.artemis.managers.TagManager
import com.artemis.{Aspect, BaseSystem, ComponentMapper}
import net.abesto.labyrinth.{Constants, Helpers}
import net.abesto.labyrinth.components.{MazeComponent, PopupTriggerComponent, PositionComponent}
import net.abesto.labyrinth.events.{HasWalkedEvent, ShowPopupEvent}
import net.abesto.labyrinth.maze.ShallowWaterTile
import net.mostlyoriginal.api.event.common.{EventSystem, Subscribe}

import scala.collection.immutable.Queue
import scala.io.Source

class PopupTriggerSystem extends BaseSystem {
  var eventSystem: EventSystem = _
  var tagManager: TagManager = _
  var positionMapper: ComponentMapper[PositionComponent] = _
  var popupTriggerMapper: ComponentMapper[PopupTriggerComponent] = _

  var hasWalkedInbox: Queue[HasWalkedEvent] = Queue()

  @Subscribe
  def enqueueHasWalked(e: HasWalkedEvent): Unit = {
    hasWalkedInbox :+= e
  }

  def processHasWalked(e: HasWalkedEvent) {
    val position = positionMapper.get(e.entityId).coord
    Helpers.entityIdsOfAspect(world,
      Aspect.all(classOf[PositionComponent], classOf[PopupTriggerComponent])
    ).find(id => positionMapper.get(id).coord == position).foreach(entityId => {
      val trigger = popupTriggerMapper.get(entityId)
      val lines = Source.fromURL(getClass.getResource(trigger.source)).getLines()
      val title = lines.next()
      val text = lines.mkString("\n")
      eventSystem.dispatch(ShowPopupEvent(title, text))
    })
  }

  override def processSystem(): Unit = {
    hasWalkedInbox.foreach(processHasWalked)
    hasWalkedInbox = Queue()
  }
}
