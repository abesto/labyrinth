package net.abesto.labyrinth.systems

import com.artemis.managers.TagManager
import com.artemis.{Archetype, ArchetypeBuilder, BaseSystem}
import net.abesto.labyrinth.{Constants, Tiles}
import net.abesto.labyrinth.components._
import squidpony.squidmath.Coord

class EntityFactory extends BaseSystem {
  var tagManager: TagManager = _

  protected lazy val bookArchetype: Archetype = new ArchetypeBuilder().add(
    classOf[PositionComponent],
    classOf[LayerComponent],
    classOf[PopupComponent],
    classOf[TileComponent],
    classOf[PersistInMazeMarker]
  ).build(world)

  protected lazy val playerArchetype: Archetype = new ArchetypeBuilder().add(
    classOf[PositionComponent],
    classOf[LayerComponent],
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

  def player(pos: Coord): Int = {
    val entity = world.createEntity(playerArchetype)
    entity.getComponent(classOf[PositionComponent]).coord = pos
    entity.getComponent(classOf[LayerComponent]).layer = LayerComponent.Layer.Creature
    entity.getComponent(classOf[TileComponent]).kind = Tiles.Kind.Player
    tagManager.register(Constants.Tags.player, entity)
    entity.getId
  }

  override def processSystem(): Unit = {}
}
