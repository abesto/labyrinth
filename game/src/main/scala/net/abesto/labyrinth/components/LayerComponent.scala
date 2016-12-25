package net.abesto.labyrinth.components

import com.badlogic.ashley.core.Component

case class LayerComponent(layer: LayerComponent.Layers.Value) extends Component {
}

object LayerComponent {
  object Layers extends Enumeration {
    // First layer that has entities on any given tile shall be rendered
    val Creature, Item = Value
  }
}
