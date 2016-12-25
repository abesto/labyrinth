package net.abesto.labyrinth.components

import com.artemis.Component
import net.abesto.labyrinth.Tiles

class TileComponent extends Component {
  var kind: Tiles.Kind = _

  def this(kind: Tiles.Kind) {
    this()
    this.kind = kind
  }
}
