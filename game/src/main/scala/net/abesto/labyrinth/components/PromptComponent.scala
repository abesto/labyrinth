package net.abesto.labyrinth.components

import java.awt.Color

import asciiPanel.AsciiPanel
import com.artemis.Component
import com.artemis.annotations.Transient

@Transient
class PromptComponent extends Component {
  var _prompt: String = ""
  def prompt: String = _prompt
  def prompt_=(s: String): Unit = {
    _prompt = s
  }

  var input: String = ""
  var cursorPosition: Int = 0
  var fgColor: Color = _
  var bgColor: Color = _

  def reset(): Unit = {
    prompt = ""
    input = ""
    cursorPosition = 0
    fgColor = AsciiPanel.brightWhite
    bgColor = AsciiPanel.black
  }

  def error(msg: String): Unit = {
    fgColor = AsciiPanel.brightWhite
    bgColor = AsciiPanel.brightRed
    prompt = msg
    input = ""
  }
}

