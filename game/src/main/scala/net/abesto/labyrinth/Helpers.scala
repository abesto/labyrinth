package net.abesto.labyrinth

import java.util.function.Consumer

import com.artemis.io.JsonArtemisSerializer
import com.artemis.managers.TagManager
import com.artemis.{Aspect, AspectSubscriptionManager, ComponentMapper, World}
import com.esotericsoftware.jsonbeans.{Json, JsonSerializer, JsonValue}
import net.abesto.labyrinth.components.LayerComponent.Layer
import net.abesto.labyrinth.components.{LayerComponent, MazeComponent, PositionComponent}
import net.abesto.labyrinth.maze.Maze
import org.reflections.Reflections
import squidpony.squidmath.Coord

import scala.collection.immutable.IndexedSeq

object Helpers {
  var aspectSubscriptionManager: AspectSubscriptionManager = _
  var tagManager: TagManager = _
  var positionMapper: ComponentMapper[PositionComponent] = _
  var layerMapper: ComponentMapper[LayerComponent] = _
  var mazeMapper: ComponentMapper[MazeComponent] = _

  var injected: World = _
  def inject(world: World): Unit = {
    if (injected != world) {
      world.inject(this)
      injected = world
    }
  }

  def entityIdsOfAspect(world: World, aspectBuilder: Aspect.Builder): IndexedSeq[Int] = {
    inject(world)
    val entityIdsBag = aspectSubscriptionManager.get(aspectBuilder).getEntities
    0.until(entityIdsBag.size).map(entityIdsBag.get)
  }

  def entityIdsAtPosition(world: World, layer: Layer, coord: Coord): Seq[Int] = {
    inject(world)
    entityIdsOfAspect(world,
      Aspect.all(classOf[PositionComponent], classOf[LayerComponent])
    ).filter(
      id => layerMapper.get(id).layer == layer && positionMapper.get(id).coord.equals(coord)
    )
  }

  def maze(world: World): Maze = {
    inject(world)
    mazeMapper.get(tagManager.getEntityId(Constants.Tags.maze)).maze
  }

  def playerEntityId(world: World): Int = {
    inject(world)
    tagManager.getEntityId(Constants.Tags.player)
  }

  protected var serializer: JsonArtemisSerializer = _

  protected def registerSerializableEnum(withName: (String) => Any, values: IndexedSeq[Any]): Unit = values.foreach(entry =>
    serializer.register(entry.getClass, new JsonSerializer[Any] {
      override def write(json: Json, `object`: Any, knownType: Class[_]): Unit = {
        json.writeObjectStart()
        json.writeValue("class", `object`.getClass.getName)
        json.writeObjectEnd()
      }
      override def read(json: Json, jsonData: JsonValue, `type`: Class[_]): Any = entry
    })
  )

  def setSerializer(s: JsonArtemisSerializer): Unit = {
    serializer = s
    // Black magic to register all ArtemisJsonEnumEntry enums into the serializer
    val r = new Reflections("net.abesto.labyrinth")
    import scala.reflect.runtime.{universe => ru}
    val runtimeMirror = ru.runtimeMirror(getClass.getClassLoader)
    r.getSubTypesOf(classOf[ArtemisJsonEnumEntry]).iterator().forEachRemaining(new Consumer[Class[_ <: ArtemisJsonEnumEntry]] {
      override def accept(t: Class[_ <: ArtemisJsonEnumEntry]): Unit = {
        if (t.getSuperclass != classOf[ArtemisJsonEnumEntry]) {
          return
        }
        val module = runtimeMirror.staticModule(t.getName)
        val companionObj = runtimeMirror.reflectModule(module).instance
        val companionCls = companionObj.getClass
        val field = companionCls.getDeclaredField("values")
        val withNameMethod = companionCls.getDeclaredMethod("withName", classOf[String])
        def withName(s: String) = withNameMethod.invoke(companionObj, s)
        field.setAccessible(true)
        registerSerializableEnum(withName, field.get(companionObj).asInstanceOf[IndexedSeq[Any]])
      }
    })
  }
}
