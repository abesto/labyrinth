package net.abesto.labyrinth.components

import com.artemis.Component
import com.artemis.annotations.Transient

@Transient
class SpellInputComponent extends Component {
  var isActive: Boolean = false
  var prompt: String = ""
  var input: String = ""
  var cursorPosition: Int = 0
}

