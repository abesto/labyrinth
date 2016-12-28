package net.abesto.labyrinth.magic

abstract class SpellWord {
  var string: String = ""

  def withString(s: String): SpellWord = {
    string = s
    this
  }

  override def toString: String = string
}
