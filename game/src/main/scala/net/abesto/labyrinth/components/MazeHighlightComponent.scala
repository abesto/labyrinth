package net.abesto.labyrinth.components

import java.awt.Color

import com.artemis.Component
import enumeratum._
import squidpony.squidmath.Coord

import scala.collection.immutable.{IndexedSeq, ListMap}

class MazeHighlightComponent extends Component {
  import MazeHighlightComponent._
  protected var highlights: ListMap[Type, Seq[Coord]] = ListMap(MazeHighlightComponent.Type.values.map(
    _ -> Seq[Coord]()
  ):_*)

  def set(t: Type, coords: Seq[Coord]): Unit =  {
    highlights += (t -> coords)
  }

  def clear(t: Type): Unit = {
    highlights += (t -> Seq[Coord]())
  }

  def get(t: Type): Seq[Coord] = {
    highlights(t)
  }

  def highlight(c: Coord): Option[Color] = highlights.find(p => get(p._1).contains(c)).map(_._1.color)
}

object MazeHighlightComponent {
  sealed abstract class Type(val color: Color) extends EnumEntry

  object Type extends Enum[Type] {
    val values: IndexedSeq[Type] = findValues

    case object SpellTarget extends Type(Color.green.darker().darker())
    case object EditorCursor extends Type(Color.lightGray)
  }
}
