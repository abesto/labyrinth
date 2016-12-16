package net.abesto.labyrinth.actions
import com.badlogic.ashley.core.{ComponentMapper, Engine, Entity}
import net.abesto.labyrinth.components.PositionComponent

case class WalkAction(x: Int, y: Int) extends Action {
  val pm = ComponentMapper.getFor(classOf[PositionComponent])

  override def apply(engine: Engine, entity: Entity): Unit = {
    val oldPosition = pm.get(entity)
    val newPosition = PositionComponent(oldPosition.x + x, oldPosition.y + y)
    entity.add(newPosition)
    println("applywalk", entity, pm.get(entity))
  }
}
