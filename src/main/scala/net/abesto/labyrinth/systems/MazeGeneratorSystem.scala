package net.abesto.labyrinth.systems

import com.badlogic.ashley.core.EntitySystem
import net.abesto.labyrinth.{EngineAccessors, Tiles}
import net.abesto.labyrinth.components.PositionComponent
import net.abesto.labyrinth.map.{FloorTile, MapComponent, WallTile}
import squidpony.squidgrid.mapping.{DungeonGenerator, DungeonUtility, GrowingTreeMazeGenerator}

import scala.util.Random

class MazeGeneratorSystem extends EntitySystem {
  var shouldGenerate = true
  val width = 80
  val height = 24

  // squidlib uses unicode, but we use asciipanel + dwarfortress tiles. translate.
  val unicodeToAsciiDrawingCharacters = Map(
    '│' -> Tiles.smoothWallNS,
    '─' -> Tiles.smoothWallEW,

    '└' -> Tiles.smoothWallSW,
    '┌' -> Tiles.smoothWallNW,
    '┘' -> Tiles.smoothWallSE,
    '┐' -> Tiles.smoothWallNE,

    '├' -> Tiles.smoothWallNSE,
    '┤' -> Tiles.smoothWallNSW,
    '┬' -> Tiles.smoothWallEWS,
    '┴' -> Tiles.smoothWallEWN,

    '┼' -> Tiles.smoothWallCross
  )

  def generate(): Unit = {
    shouldGenerate = true
  }

  override def update(deltaTime: Float): Unit = {
    if (shouldGenerate) {
      val mapEntity = EngineAccessors.mapEntity(getEngine)
      val maze = DungeonUtility.hashesToLines(new DungeonGenerator(width, height).generate())
      val mapComponent = MapComponent(
        maze.zipWithIndex.map {
          case (column, x) => column.zipWithIndex.map {
            case (c, y) => if (c == '.') {
              new FloorTile(x, y, Tiles.roughFloors(Random.nextInt(Tiles.roughFloors.length)))
            } else {
              new WallTile(x, y, unicodeToAsciiDrawingCharacters.getOrElse(c, c))
            }
          }
        }
      )
      mapEntity.add(mapComponent)

      val startPos = new DungeonUtility().randomFloor(mapComponent.chars)
      EngineAccessors.player(getEngine).add(PositionComponent(startPos))

      shouldGenerate = false
    }
  }
}
