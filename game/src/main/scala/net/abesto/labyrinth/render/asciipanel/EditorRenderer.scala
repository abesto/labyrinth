package net.abesto.labyrinth.render.asciipanel

import net.abesto.labyrinth.Constants

class EditorRenderer() extends Renderer(
  new MazeFrame(
    0, Constants.messageAreaHeight,
    Constants.mazeWidth, Constants.mazeHeight),
  new MessageAreaFrame(
    0, 0,
    Constants.fullWidth, Constants.messageAreaHeight)
)
