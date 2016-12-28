package net.abesto.labyrinth.magic

import com.artemis.World
import squidpony.squidmath.Coord

abstract class SpellTarget extends SpellWord {
  def affectedTiles(world: World): Seq[Coord] = {
    world.inject(this)
    affectedTiles()
  }

  def affectedTiles(): Seq[Coord]

  def compose(lh: SpellTarget): SpellTarget
}
