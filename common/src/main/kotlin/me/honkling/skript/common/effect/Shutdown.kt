package me.honkling.skript.common.effect

import me.honkling.skript.common.Skript
import me.honkling.skript.common.pattern.MatchResult
import me.honkling.skript.common.syntax.Effect
import me.honkling.skript.common.syntax.Expression
import me.honkling.skript.common.syntax.registration.SyntaxRegistration
import kotlin.system.exitProcess

class Shutdown(private val status: Expression<Int>? = null) : Effect {
    class Registration(skript: Skript) : SyntaxRegistration<Shutdown>(skript, "shutdown [with [status] [code] %integer%]") {
        override fun initialize(skript: Skript, matchResult: MatchResult): Shutdown {
            return Shutdown(matchResult.expressions.getOrNull(0) as Expression<Int>?)
        }
    }

    override fun execute() {
        val code = status?.get()?.firstOrNull() ?: 0
        exitProcess(code)
    }
}