package net.abesto.labyrinth.systems

import com.badlogic.ashley.core.EntitySystem
import net.abesto.labyrinth.EngineAccessors
import net.abesto.labyrinth.components.PositionComponent
import net.abesto.labyrinth.map.{FloorTile, MapComponent, WallTile}

import scala.io.Source

class MapLoaderSystem extends EntitySystem {
  var toLoad: Option[String] = None

  def load(name: String): Unit = {
    toLoad = Some(name)
  }

  def charToMapTile(c: Char, x: Int, y: Int) = c match {
    case '.' => new FloorTile(x, y)
    case '#' => new WallTile(x, y)
    case '@' =>
      EngineAccessors.player(getEngine).add(PositionComponent(x, y))
      new FloorTile(x, y)
  }

  override def update(deltaTime: Float): Unit = {
    if (toLoad.isDefined) {
      val mapEntity = EngineAccessors.mapEntity(getEngine)
      mapEntity.add(MapComponent(
        Source.fromURL(getClass.getResource(s"/maps/${toLoad.get}")).getLines().zipWithIndex.map {
          case (line, y) => line.zipWithIndex.map {
            case (char, x) => charToMapTile(char, x, y)
          }.toArray
        }.toArray.transpose
      ))
      toLoad = None
    }
  }
}
