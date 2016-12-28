package net.abesto.labyrinth.magic

class SpellWordList(val words: Seq[SpellWord]) {
  def words[T](implicit mf: Manifest[T]): Seq[T] = words.collect { case w: T => w }
}
