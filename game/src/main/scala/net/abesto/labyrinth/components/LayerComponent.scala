package net.abesto.labyrinth.components

import com.artemis.Component
import enumeratum._
import net.abesto.labyrinth.ArtemisJsonEnumEntry

import scala.collection.immutable.IndexedSeq


class LayerComponent() extends Component {
  var layer: LayerComponent.Layer = _

  def this(layer: LayerComponent.Layer) {
    this()
    this.layer = layer
  }
}

object LayerComponent {
  sealed abstract class Layer extends ArtemisJsonEnumEntry(Layer)

  object Layer extends Enum[Layer] {
    val values: IndexedSeq[Layer] = findValues

    // First layer that has entities on any given tile shall be rendered
    case object Creature extends Layer
    case object Item extends Layer
  }
}
