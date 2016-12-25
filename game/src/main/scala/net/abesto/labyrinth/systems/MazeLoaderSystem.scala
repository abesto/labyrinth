package net.abesto.labyrinth.systems

import com.artemis.io.SaveFileFormat
import com.artemis.managers.{TagManager, WorldSerializationManager}
import com.artemis.{BaseSystem, ComponentMapper}
import net.abesto.labyrinth.components.{MazeComponent, PositionComponent}
import net.abesto.labyrinth.maze._
import net.abesto.labyrinth.{Constants, Tiles}
import squidpony.squidmath.Coord

class MazeLoaderSystem extends BaseSystem {
  var tagManager: TagManager = _
  var serializationManager: WorldSerializationManager = _
  var mazeMapper: ComponentMapper[MazeComponent] = _
  var positionMapper: ComponentMapper[PositionComponent] = _

  var toLoad: Option[String] = None

  def load(name: String): Unit = {
    toLoad = Some(name)
  }

  def tileCallback(k: Tiles.Kind, x: Int, y: Int): Unit = k match {
    case Tiles.Kind.Player =>
      positionMapper.get(tagManager.getEntityId(Constants.Tags.player)).coord = Coord.get(x, y)
    case _ =>
  }

  override def checkProcessing(): Boolean = toLoad.isDefined

  override def processSystem(): Unit = {
    val mazeEntityId = tagManager.getEntityId(Constants.Tags.maze)
    val mazeComponent = mazeMapper.get(mazeEntityId)
    mazeComponent.maze = MazeBuilder.fromFile(toLoad.get, tileCallback).hashesToLines().get
    serializationManager.load(getClass.getResourceAsStream(s"/maps/${toLoad.get}.json"), classOf[SaveFileFormat])
    toLoad = None
  }
}
