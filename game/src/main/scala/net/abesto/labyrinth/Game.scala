package net.abesto.labyrinth

import net.abesto.labyrinth.components.{LayerComponent, PopupTriggerComponent, PositionComponent, TileComponent}
import net.abesto.labyrinth.render.asciipanel.AsciiPanelRenderer
import net.abesto.labyrinth.systems.MazeLoaderSystem

import scala.io.Source

object Game {
  def main(args: Array[String]): Unit = {
    val renderer = AsciiPanelRenderer()
    val world = WorldDefinition.buildWorld(renderer)

    val app = new ApplicationMain(world, renderer)
    app.setup()

    // Kick things off
    world.getSystem(classOf[MazeLoaderSystem]).load("1-dry")

    // This next part is to be replaced by a proper item placement and triggering system
    val book = world.createEntity()
    book.edit()
      .add(new PositionComponent(35, 10))
      .add(new LayerComponent(LayerComponent.Layers.Item))
      .add(new TileComponent(Tiles.Kind.Book))
      .add(new PopupTriggerComponent(
        "Lorem Ipsum",
        Source.fromURL(getClass.getResource("/books/1-dry")).getLines().mkString("\n")
      ))
    // EOF hack

    world.setDelta(1)
    world.process()
  }
}
