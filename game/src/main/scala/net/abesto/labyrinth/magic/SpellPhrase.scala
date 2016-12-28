package net.abesto.labyrinth.magic

class SpellPhrase(words: Seq[SpellWord] = Seq.empty) extends SpellWordList(words) {
  def hasExactlyOneEffect: Boolean = words[SpellEffect].length == 1
  def hasConflicts: Boolean = words.combinations(2).exists(c => {
    val Seq(a, b) = c
    conflicts(a, b)
  })

  def isValid: Boolean = hasExactlyOneEffect && !hasConflicts

  def conflicts(a: SpellWord, b: SpellWord, symmetricRetry: Boolean = false): Boolean = (a, b) match {
    case (a: SpellEffect, b: SpellEffect) => true
    case (AreaTarget.caster, b: AreaTarget) => true
    case _ if !symmetricRetry => conflicts(b, a, symmetricRetry = true)
    case _ => false
  }

  def buildSpell(): Option[Spell] = {
    if (!isValid) {
      None
    } else {
      val s = new Spell
      s.effect = words[SpellEffect].head
      s.target = words[SpellTarget].foldLeft(s.target)((rh, lh) => lh.compose(rh))
      s.words = words
      Some(s)
    }
  }
}
