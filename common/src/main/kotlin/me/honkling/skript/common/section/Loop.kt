package me.honkling.skript.common.section

import me.honkling.skript.common.Skript
import me.honkling.skript.common.pattern.MatchResult
import me.honkling.skript.common.syntax.Expression
import me.honkling.skript.common.syntax.Section
import me.honkling.skript.common.syntax.registration.SyntaxRegistration

class Loop(private val times: Expression<Int>) : Section() {
    class Registration(skript: Skript) : SyntaxRegistration<Loop>(
        skript,
        "loop %integer% times"
    ) {
        override fun initialize(skript: Skript, matchResult: MatchResult): Loop {
            return Loop(matchResult.expressions[0] as Expression<Int>)
        }
    }

    override fun walk(): Boolean {
        val times = times.get()?.firstOrNull() ?: return false
        repeat(times - 1) {
            block.execute()
        }
        return true
    }
}