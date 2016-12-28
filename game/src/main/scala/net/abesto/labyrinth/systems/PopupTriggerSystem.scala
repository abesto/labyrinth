package net.abesto.labyrinth.systems

import com.artemis.managers.TagManager
import com.artemis.{Aspect, BaseSystem, ComponentMapper}
import net.abesto.labyrinth.Helpers
import net.abesto.labyrinth.components.{PopupTriggerComponent, PositionComponent}
import net.abesto.labyrinth.events.{HasWalkedEvent, ShowPopupEvent}
import net.abesto.labyrinth.macros.{DeferredEventHandlerSystem, DeferredEventHandlerSystemImpl, SubscribeDeferred}
import net.mostlyoriginal.api.event.common.EventSystem

import scala.io.Source

@DeferredEventHandlerSystem
class PopupTriggerSystem extends BaseSystem {
  var eventSystem: EventSystem = _
  var tagManager: TagManager = _
  var positionMapper: ComponentMapper[PositionComponent] = _
  var popupTriggerMapper: ComponentMapper[PopupTriggerComponent] = _

  @SubscribeDeferred
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
}
