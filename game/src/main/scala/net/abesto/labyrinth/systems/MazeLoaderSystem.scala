package net.abesto.labyrinth.systems

import com.badlogic.ashley.core.EntitySystem
import net.abesto.labyrinth.{EngineAccessors, Tiles}
import net.abesto.labyrinth.components.PositionComponent
import net.abesto.labyrinth.maze._

class MazeLoaderSystem extends EntitySystem {
  var toLoad: Option[String] = None

  def load(name: String): Unit = {
    toLoad = Some(name)
  }

  def tileCallback(k: Tiles.Kind.Value, x: Int, y: Int): Unit = k match {
    case Tiles.Kind.Player =>
      EngineAccessors.player(getEngine).add(PositionComponent(x, y))
    case _ =>
  }

  override def update(deltaTime: Float): Unit = {
    if (toLoad.isDefined) {
      val mapEntity = EngineAccessors.mapEntity(getEngine)
      mapEntity.add(
        MazeComponent(MazeBuilder.fromFile(toLoad.get, tileCallback).hashesToLines().get
      ))
      toLoad = None
    }
  }
}
