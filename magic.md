# The laws of magic

## Spoiler alert: this is a design document, not a game manual

1. A spell consists of two parts: an effect, and a target
1. The target can be the caster, an area, or a direction
1. Area-targeting spells are not limited by line of sight (but this is not common knowledge)
1. Directional spells act on the first non-empty tile they "hit" based on the casting direction
1. Spells are cast by typing "sentences" - let's call them spell-phrases
1. Each spell-phrase must have an effect. An effect in itself is a valid spell, and acts on the caster (or the tile the caster is standing on).
1. A spell-phrase can have any number of words defining the target, as long as they don't contradict each other
1. Magic users are limited in the number of words they can include in a cast spell-phrase by how practiced they are

## Examples

If the language of magic was English (as opposed to a mix of dead and fantasy languages).

### Valid spells

 * `dry` - dries the clothes of the user
 * `pull three east` - pull something that's three tiles to the east of the caster - a lever, or an item (towards the caster, in the case of an item)
 * `fire ray south` - shoot a jet of flames southward
 * `rain south two far three wide five deep` - extinguish any open flames in a 3x5 rectangle 2 tiles south of the caster

### Invalid spells

 * `dry rain` - both words are effects, and a spell-phrase can only have one effect
 * `pull east west` - the two words `east` and `west` both define the target of the spell, but are contradictory

