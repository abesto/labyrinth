package net.abesto.labyrinth.render.asciipanel

import net.abesto.labyrinth.Constants

class Renderer() extends RendererImpl(
  new MazeFrame(
    0, Constants.messageAreaHeight,
    Constants.mazeWidth, Constants.mazeHeight),
  new MessageAreaFrame(
    0, 0,
    Constants.fullWidth, Constants.messageAreaHeight),
  new Popup,
  new PromptFrame(
    0, Constants.fullHeight - Constants.castingPromptHeight,
    Constants.fullWidth, Constants.castingPromptHeight),
  new MainMenuFrame(0, 0, Constants.fullWidth, Constants.fullHeight),
  new EditorActionsFrame(
    Constants.mazeWidth, Constants.messageAreaHeight,
    Constants.sidebarWidth, Constants.mazeHeight
  )
)
