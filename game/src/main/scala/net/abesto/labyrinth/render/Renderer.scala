package net.abesto.labyrinth.render

import javax.swing.JPanel

import com.artemis.BaseSystem

trait Renderer extends BaseSystem {
  val panel: JPanel
}
