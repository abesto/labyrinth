package net.abesto.labyrinth.magic
import com.artemis.ComponentMapper
import com.artemis.managers.TagManager
import net.abesto.labyrinth.Constants
import net.abesto.labyrinth.components.PositionComponent
import squidpony.squidmath.Coord

case class AreaTarget(offset: Coord, size: Coord) extends SpellTarget {
  var tagManager: TagManager = _
  var positionMapper: ComponentMapper[PositionComponent] = _

  override def compose(lh: SpellTarget): SpellTarget = lh match {
    case lh: AreaTarget => AreaTarget(
      lh.offset.add(offset),
      Coord.get(math.max(lh.size.getX, size.getX), math.max(lh.size.getY, size.getY))
    )
  }

  // For now, I'm bailing out on the relatively hard math of rotating and centering the impact area by the offset
  // and just implementing this for single tile target areas
  override def affectedTiles(): Seq[Coord] =
    Seq(
      positionMapper.get(tagManager.getEntityId(Constants.Tags.player)).coord.add(offset)
    )
}

object AreaTarget {
  val caster = AreaTarget(Coord.get(0, 0), Coord.get(1, 1))
}
