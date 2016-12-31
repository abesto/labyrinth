package net.abesto.labyrinth.maze

import java.awt.Color

import asciiPanel.{AsciiCharacterData, AsciiPanel}

import scala.language.implicitConversions

class CharacterData(c: Char, fg: Color = AsciiPanel.white, bg: Color = AsciiPanel.black) extends AsciiCharacterData {
  character = c
  foregroundColor = fg
  backgroundColor = bg
}

object CharacterData {
  implicit def fromChar(c: Char): CharacterData = new CharacterData(c)
}
