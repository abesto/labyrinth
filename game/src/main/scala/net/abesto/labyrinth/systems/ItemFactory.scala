package net.abesto.labyrinth.systems

import com.artemis.{Archetype, ArchetypeBuilder, BaseSystem}
import net.abesto.labyrinth.Tiles
import net.abesto.labyrinth.components._
import squidpony.squidmath.Coord

class ItemFactory extends BaseSystem {
  protected lazy val bookArchetype: Archetype = new ArchetypeBuilder().add(
    classOf[PositionComponent],
    classOf[LayerComponent],
    classOf[PopupTriggerComponent],
    classOf[TileComponent],
    classOf[PersistInMazeMarker]
  ).build(world)

  def book(pos: Coord): Int = {
    val entity = world.createEntity(bookArchetype)
    entity.getComponent(classOf[PositionComponent]).coord = pos
    entity.getComponent(classOf[LayerComponent]).layer = LayerComponent.Layer.Item
    entity.getComponent(classOf[TileComponent]).kind = Tiles.Kind.Book
    entity.getId
  }

  override def processSystem(): Unit = {}
}
