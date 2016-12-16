package net.abesto.labyrinth.render

import javax.swing.JPanel

trait Renderer {
  val panel: JPanel
  def render()
}
