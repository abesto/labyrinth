package net.abesto.labyrinth.systems

import com.badlogic.ashley.core.{ComponentMapper, Entity, Family}
import com.badlogic.ashley.systems.IteratingSystem
import net.abesto.labyrinth.components.ActionQueueComponent

class ActionQueueSystem extends IteratingSystem(Family.all(classOf[ActionQueueComponent]).get) {
  val aqm = ComponentMapper.getFor(classOf[ActionQueueComponent])

  override def processEntity(entity: Entity, deltaTime: Float): Unit = {
    println(entity)
    val actions = aqm.get(entity).actions
    val engine = getEngine
    while (actions.nonEmpty) {
      actions.dequeue()(engine, entity)
    }
  }
}
