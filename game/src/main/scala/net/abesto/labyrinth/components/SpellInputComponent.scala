package net.abesto.labyrinth.components

import com.artemis.Component
import com.artemis.annotations.Transient
import net.abesto.labyrinth.magic.Spell

@Transient
class SpellInputComponent extends Component {
  var prompt: String = ""
  var input: String = ""
  var cursorPosition: Int = 0
  var spell: Option[Spell] = None
}

