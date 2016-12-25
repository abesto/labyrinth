package net.abesto.labyrinth.components

import com.artemis.Component

class PopupTriggerComponent extends Component {
  var title: String = ""
  var text: String = ""

  def this(title: String, text: String) {
    this()
    this.title = title
    this.text = text
  }
}
