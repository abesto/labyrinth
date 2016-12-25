package net.abesto.labyrinth.components

import com.artemis.Component


class LayerComponent() extends Component {
  var layer: LayerComponent.Layers.Value = _

  def this(_layer: LayerComponent.Layers.Value) {
    this()
    layer = _layer
  }
}

object LayerComponent {
  object Layers extends Enumeration {
    // First layer that has entities on any given tile shall be rendered
    val Creature, Item = Value
  }
}
