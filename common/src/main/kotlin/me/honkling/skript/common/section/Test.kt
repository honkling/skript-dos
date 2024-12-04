package me.honkling.skript.common.section

import me.honkling.skript.common.Skript
import me.honkling.skript.common.pattern.MatchResult
import me.honkling.skript.common.syntax.Section
import me.honkling.skript.common.syntax.registration.SyntaxRegistration

class Test : Section() {
    class Registration(skript: Skript) : SyntaxRegistration<Test>(
        skript,
        "test section"
    ) {
        override fun initialize(skript: Skript, matchResult: MatchResult): Test {
            return Test()
        }
    }

    override fun walk(): Boolean {
        return true
    }
}