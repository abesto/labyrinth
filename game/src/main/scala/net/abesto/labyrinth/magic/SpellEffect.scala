package net.abesto.labyrinth.magic

import com.artemis.{Entity, World}
import squidpony.squidmath.Coord

abstract class SpellEffect extends SpellWord {
  protected var world: World = _

  def applyToEntities(target: Seq[Int], world: World): Unit = {
    world.inject(this)
    this.world = world
    applyToEntities(target)
  }

  def applyToMaze(target: Seq[Coord], world: World): Unit = {
    world.inject(this)
    this.world = world
    applyToMaze(target)
  }

  protected def applyToEntities(target: Seq[Int]): Unit
  protected def applyToMaze(target: Seq[Coord]): Unit
}
