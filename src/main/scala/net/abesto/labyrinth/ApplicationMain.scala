package net.abesto.labyrinth

import javax.swing.JFrame

import net.abesto.labyrinth.render.Renderer

class ApplicationMain(renderer: Renderer) extends JFrame {
  add(renderer.panel)
  pack()
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
}
