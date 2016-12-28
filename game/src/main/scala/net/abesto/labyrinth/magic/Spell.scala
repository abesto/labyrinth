package net.abesto.labyrinth.magic

import com.artemis.World
import net.abesto.labyrinth.Helpers
import net.abesto.labyrinth.components.LayerComponent.Layer
import squidpony.squidmath.Coord

class Spell {
  var effect: SpellEffect = _
  var target: SpellTarget = AreaTarget.caster
  var words: Seq[SpellWord] = _

  def cast(world: World): Unit = {
    val tiles = target.affectedTiles(world)
    val entities = tiles.flatMap(Helpers.entityIdsAtPosition(world, Layer.Creature, _))
    if (entities.isEmpty) {
      effect.applyToMaze(tiles, world)
    } else {
      effect.applyToEntities(entities, world)
    }
  }

  override def toString: String = words.mkString(" ")
}
