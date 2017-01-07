package net.abesto.labyrinth.components

import com.artemis.Component
import com.artemis.annotations.Transient
import net.abesto.labyrinth.magic.Spell

@Transient
class PromptComponent extends Component {
  var prompt: String = ""
  var input: String = ""
  var cursorPosition: Int = 0

  def reset(): Unit = {
    prompt = ""
    input = ""
    cursorPosition = 0
  }
}

