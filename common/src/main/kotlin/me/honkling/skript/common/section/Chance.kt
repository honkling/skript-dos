package me.honkling.skript.common.section

import me.honkling.skript.common.Skript
import me.honkling.skript.common.pattern.MatchResult
import me.honkling.skript.common.syntax.Expression
import me.honkling.skript.common.syntax.Section
import me.honkling.skript.common.syntax.registration.SyntaxRegistration

class Chance(private val percentage: Expression<Int>) : Section() {
    class Registration(skript: Skript) : SyntaxRegistration<Chance>(
        skript,
        "chance of %integer%",
    ) {
        override fun initialize(skript: Skript, matchResult: MatchResult): Chance {
            return Chance(matchResult.expressions[0] as Expression<Int>)
        }
    }

    override fun walk(): Boolean {
        val percentage = percentage.get()!!.first()
        return Math.random() * 100 < percentage
    }
}