package net.abesto.labyrinth.systems

import com.artemis.ComponentMapper
import com.artemis.io.SaveFileFormat
import com.artemis.managers.{TagManager, WorldSerializationManager}
import net.abesto.labyrinth.components.{MazeComponent, PositionComponent}
import net.abesto.labyrinth.events.LoadMazeEvent
import net.abesto.labyrinth.macros.{SubscribeDeferred, SubscribeDeferredContainer}
import net.abesto.labyrinth.maze._
import net.abesto.labyrinth.{Constants, Tiles}
import squidpony.squidmath.Coord


@SubscribeDeferredContainer
class MazeLoaderSystem extends EventHandlerSystem {
  var tagManager: TagManager = _
  var serializationManager: WorldSerializationManager = _
  var mazeMapper: ComponentMapper[MazeComponent] = _
  var positionMapper: ComponentMapper[PositionComponent] = _

  def tileCallback(k: Tiles.Kind, x: Int, y: Int): Unit = k match {
    case Tiles.Kind.Player =>
      positionMapper.get(tagManager.getEntityId(Constants.Tags.player)).coord = Coord.get(x, y)
    case _ =>
  }

  @SubscribeDeferred
  def load(e: LoadMazeEvent): Unit = {
    val mazeEntityId = tagManager.getEntityId(Constants.Tags.maze)
    val mazeComponent = mazeMapper.get(mazeEntityId)
    mazeComponent.maze = MazeBuilder.fromFile(e.name, tileCallback).hashesToLines().get
    serializationManager.load(getClass.getResourceAsStream(s"/maps/${e.name}.json"), classOf[SaveFileFormat])
  }
}
