package net.abesto.labyrinth.components

import com.artemis.Component

import scala.io.Source

class PopupComponent extends Component {
  var title: String = ""
  var text: String = ""

  private var _source: String = ""
  def source: String = _source
  def source_=(s: String): Unit = {
    _source = s
    val lines = Source.fromURL(getClass.getResource(source)).getLines()
    title = lines.next()
    text = lines.mkString("\n")
  }
}
