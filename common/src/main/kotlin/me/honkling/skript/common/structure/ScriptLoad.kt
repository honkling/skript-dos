package me.honkling.skript.common.structure

import me.honkling.skript.common.Skript
import me.honkling.skript.common.pattern.MatchResult
import me.honkling.skript.common.syntax.Structure
import me.honkling.skript.common.syntax.registration.SyntaxRegistration

class ScriptLoad : Structure() {
    class Registration(
        skript: Skript
    ) : SyntaxRegistration<ScriptLoad>(skript, "on script load") {
        override fun initialize(skript: Skript, matchResult: MatchResult): ScriptLoad {
            return ScriptLoad()
        }
    }

    override fun load() {
        execute()
    }
}