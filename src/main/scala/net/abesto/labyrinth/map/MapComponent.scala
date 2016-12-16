package net.abesto.labyrinth.map

import com.badlogic.ashley.core.Component

class MapComponent extends Component {
  val WIDTH = 80
  val HEIGHT = 24

  val tiles = 0.until(HEIGHT).map(y =>
    0.until(WIDTH).map(x =>
      MapTile(x, y)
    )
  )
}
