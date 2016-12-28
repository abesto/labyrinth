package net.abesto.labyrinth.magic

import scala.util.parsing.combinator._

class SpellParser(d: SpellWordList) extends RegexParsers {
  def effect: Parser[SpellEffect] = d.words[SpellEffect].map(e =>
    literal(e.string) ^^ (_ => e)
  ).reduce(_ | _)

  def target: Parser[SpellTarget] = d.words[SpellTarget].map(t =>
    literal(t.string) ^^ (_ => t)
  ).reduce(_ | _)

  def phrase: Parser[SpellPhrase] = effect ~ target.* ^^ ((e) => new SpellPhrase(e._1 +: e._2))

  def spell: Parser[Option[Spell]] = phrase ^^ (_.buildSpell())

  def parse(s: String): Option[Spell] = parseAll(spell, s).getOrElse(None)
}
