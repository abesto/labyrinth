package net.abesto.labyrinth.systems

import java.util.function.Consumer

import com.artemis._
import com.artemis.annotations.AspectDescriptor
import com.artemis.io.JsonArtemisSerializer
import com.artemis.managers.TagManager
import com.artemis.utils.IntBag
import com.esotericsoftware.jsonbeans.{Json, JsonSerializer, JsonValue}
import net.abesto.labyrinth.components.LayerComponent.Layer
import net.abesto.labyrinth.components._
import net.abesto.labyrinth.fsm.InStates
import net.abesto.labyrinth.maze.Maze
import net.abesto.labyrinth.{ArtemisJsonEnumEntry, Constants}
import org.reflections.Reflections
import squidpony.squidmath.Coord

import scala.collection.immutable.IndexedSeq


class Helpers extends BaseSystem {
  protected var tagManager: TagManager = _
  protected var positionMapper: ComponentMapper[PositionComponent] = _
  protected var layerMapper: ComponentMapper[LayerComponent] = _
  protected var mazeMapper: ComponentMapper[MazeComponent] = _
  protected var stateMapper: ComponentMapper[StateComponent] = _
  protected var highlightMapper: ComponentMapper[MazeHighlightComponent] = _
  protected var promptMapper: ComponentMapper[PromptComponent] = _

  @AspectDescriptor(all=Array(classOf[PositionComponent], classOf[LayerComponent]))
  protected var positionLayerAspect: Aspect.Builder = _
  protected var aspectSubscriptionManager: AspectSubscriptionManager = _

  def entityIds(aspectBuilder: Aspect.Builder): IntBag = aspectSubscriptionManager.get(aspectBuilder).getEntities

  def entityIdsSeq(aspectBuilder: Aspect.Builder): IndexedSeq[Int] = {
    val entityIdsBag = entityIds(aspectBuilder)
    0.until(entityIdsBag.size).map(entityIdsBag.get)
  }

  def entityIdsAtPosition(layer: Layer, coord: Coord): Seq[Int] = {
    entityIdsSeq(positionLayerAspect).filter(
      id => try {
        layerMapper.get(id).layer == layer && positionMapper.get(id).coord.equals(coord)
      } catch {
        case _: NullPointerException => false
      }
    )
  }

  def entitiesAtPosition(layer: Layer, coord: Coord): Seq[Entity] = {
    entityIdsAtPosition(layer, coord).map(world.getEntity)
  }

  def mazeEntityId: Int = tagManager.getEntityId(Constants.Tags.maze)
  def mazeComponent: MazeComponent = mazeMapper.get(mazeEntityId)
  def maze: Maze = mazeComponent.maze
  def highlight: MazeHighlightComponent = highlightMapper.get(mazeEntityId)

  def playerEntityId: Int = tagManager.getEntityId(Constants.Tags.player)
  def state: StateComponent = stateMapper.get(tagManager.getEntityId(Constants.Tags.state))

  def prompt: PromptComponent = promptMapper.get(tagManager.getEntityId(Constants.Tags.prompt))

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

  val clsInStates: Class[InStates] = classOf[InStates]
  def isEnabledInCurrentState(o: Any): Boolean = {
    val clsState = state.current.getClass
    val cls = o.getClass
    Option(cls.getAnnotation(clsInStates)).forall(_.value().exists(_.isAssignableFrom(clsState)))
  }

  override def processSystem(): Unit = {}
}
