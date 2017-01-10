package net.abesto.labyrinth.systems

import com.artemis.annotations.AspectDescriptor
import com.artemis.managers.TagManager
import com.artemis.{Aspect, ComponentMapper}
import net.abesto.labyrinth.components.{PopupComponent, PositionComponent}
import net.abesto.labyrinth.events.HasWalkedEvent
import net.abesto.labyrinth.fsm.Transitions
import net.abesto.labyrinth.macros.{DeferredEventHandlerSystem, SubscribeDeferred}
import net.mostlyoriginal.api.event.common.EventSystem

import scala.io.Source

@DeferredEventHandlerSystem
class PopupTriggerSystem extends LabyrinthBaseSystem {
  var eventSystem: EventSystem = _
  var tagManager: TagManager = _
  var positionMapper: ComponentMapper[PositionComponent] = _
  var popupMapper: ComponentMapper[PopupComponent] = _
  var helpers: Helpers = _

  @AspectDescriptor(all = Array(classOf[PositionComponent], classOf[PopupComponent]))
  var popupTriggerAspect: Aspect.Builder = _

  @SubscribeDeferred
  def processHasWalked(e: HasWalkedEvent) {
    val position = positionMapper.get(e.entityId).coord
    helpers.entityIdsSeq(popupTriggerAspect)
      .find(id => positionMapper.get(id).coord == position)
      .foreach(entityId => {
        eventSystem.dispatch(new Transitions.ShowPopupEvent(popupMapper.get(entityId)))
      })
  }
}
