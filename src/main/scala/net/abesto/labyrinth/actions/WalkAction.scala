package net.abesto.labyrinth.actions
import com.badlogic.ashley.core.{ComponentMapper, Engine, Entity}
import net.abesto.labyrinth.EngineAccessors
import net.abesto.labyrinth.components.PositionComponent

case class WalkAction(x: Int, y: Int) extends Action {
  val pm = ComponentMapper.getFor(classOf[PositionComponent])

  def canWalkTo(p: PositionComponent, engine: Engine): Boolean = {
    val m = EngineAccessors.map(engine)
    p.x >= 0 && p.y >= 0 && p.x < m.width && p.y < m.height && m.tile(p).canBeStoodOn
  }

  override def apply(engine: Engine, entity: Entity): Unit = {
    val oldPosition = pm.get(entity)
    val newPosition = PositionComponent(oldPosition.x + x, oldPosition.y + y)
    if (canWalkTo(newPosition, engine)) {
      entity.add(newPosition)
    }
  }
}
