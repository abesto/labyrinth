package net.abesto.labyrinth.components

import com.artemis.Component

class SpellInputComponent extends Component {
  var isActive: Boolean = false
  var prompt: String = ""
  var input: String = ""
  var cursorPosition: Int = 0
}

