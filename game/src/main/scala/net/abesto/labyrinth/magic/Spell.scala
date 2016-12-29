package net.abesto.labyrinth.magic

import com.artemis.World
import net.abesto.labyrinth.components.LayerComponent.Layer
import net.abesto.labyrinth.systems.Helpers

class Spell {
  var helpers: Helpers = _

  var effect: SpellEffect = _
  var target: SpellTarget = AreaTarget.caster
  var words: Seq[SpellWord] = _

  def cast(world: World): Unit = {
    world.inject(this)
    val tiles = target.affectedTiles(world)
    val entities = tiles.flatMap(helpers.entityIdsAtPosition(Layer.Creature, _))
    if (entities.isEmpty) {
      effect.applyToMaze(tiles, world)
    } else {
      effect.applyToEntities(entities, world)
    }
  }

  override def toString: String = words.mkString(" ")
}
