package net.abesto.labyrinth

import scala.collection.JavaConverters._
import com.badlogic.ashley.core.{ComponentMapper, Engine, Entity, Family}
import net.abesto.labyrinth.components.{LayerComponent, PlayerMarker, PositionComponent}
import net.abesto.labyrinth.maze.MazeComponent
import net.abesto.labyrinth.systems.MazeLoaderSystem
import squidpony.squidmath.Coord

object EngineAccessors {
  val mazeComponentMapper: ComponentMapper[MazeComponent] = ComponentMapper.getFor(classOf[MazeComponent])
  val positionComponentMapper: ComponentMapper[PositionComponent] = ComponentMapper.getFor(classOf[PositionComponent])
  val layerComponentMapper: ComponentMapper[LayerComponent] = ComponentMapper.getFor(classOf[LayerComponent])

  def getSingle(e: Engine, family: Family.Builder): Entity = {
    val es = e.getEntitiesFor(family.get)
    assert(es.size == 1)
    es.get(0)
  }

  def mapEntity(e: Engine): Entity = getSingle(e, Family.all(classOf[MazeComponent]))

  def maze(e: Engine): MazeComponent = {
     mazeComponentMapper.get(mapEntity(e))
  }

  def loadMap(e: Engine, name: String): Unit = {
    e.getSystem(classOf[MazeLoaderSystem]).load(name)
  }

  def player(e: Engine): Entity = getSingle(e, Family.all(classOf[PlayerMarker]))

  def entitiesAt(e: Engine, coord: Coord, layer: LayerComponent.Layers.Value): Stream[Entity] =
    e.getEntitiesFor(Family.all(classOf[PositionComponent], classOf[LayerComponent]).get).iterator()
      .asScala.toStream.filter(e =>
      positionComponentMapper.get(e).coord.equals(coord) && layerComponentMapper.get(e).layer == layer
    )
  def creaturesAt: (Engine, Coord) => Stream[Entity] = entitiesAt(_, _, LayerComponent.Layers.Creature).ensuring(_.length <= 1)
  def itemsAt: (Engine, Coord) => Stream[Entity] = entitiesAt(_, _, LayerComponent.Layers.Item)
}
