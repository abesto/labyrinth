package net.abesto.labyrinth.render

import javax.swing.JPanel

import com.badlogic.ashley.core.EntitySystem

trait Renderer extends EntitySystem {
  val panel: JPanel
}
