package net.abesto.labyrinth

import com.badlogic.ashley.core.{Engine, Entity}
import com.badlogic.ashley.signals.{Listener, Signal}
import net.abesto.labyrinth.components.{LayerComponent, PositionComponent, TileComponent}
import net.abesto.labyrinth.render.asciipanel.AsciiPanelRenderer
import net.abesto.labyrinth.signals.{PopupData, Signals}

import scala.io.Source

object Game {
  def main(args: Array[String]): Unit = {
    val engine = new Engine
    val world = new World(engine)

    val renderer = AsciiPanelRenderer()
    engine.addSystem(renderer)

    val app = new ApplicationMain(engine, renderer)
    app.setup()

    // Kick things off
    EngineAccessors.loadMap(engine, "1-dry")
    engine.update(1)

    // This next part is to be replaced by a proper item placement and triggering system
    val book = new Entity()
    book.add(PositionComponent(35, 10))
    book.add(LayerComponent(LayerComponent.Layers.Item))
    book.add(TileComponent(Tiles.Kind.Book))
    engine.addEntity(book)

    Signals.hasWalked.add(new Listener[Entity] {
      override def receive(signal: Signal[Entity], p: Entity): Unit = {
        val pos: PositionComponent = p.getComponent(classOf[PositionComponent])
        if (pos.x == 35 && pos.y == 10) {
          Signals.showPopup.dispatch(PopupData(
            "Baby Steps",
            Source.fromURL(getClass.getResource("/books/1-dry")).getLines().mkString("\n")
          ))
        }
      }
    })
    engine.update(1)
    // EOF hack
  }
}
