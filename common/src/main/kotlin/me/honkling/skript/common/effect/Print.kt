package me.honkling.skript.common.effect

import me.honkling.skript.common.Skript
import me.honkling.skript.common.pattern.MatchResult
import me.honkling.skript.common.syntax.Effect
import me.honkling.skript.common.syntax.Expression
import me.honkling.skript.common.syntax.registration.SyntaxRegistration

class Print(private val objects: Expression<Any>) : Effect {
    class Registration(skript: Skript) : SyntaxRegistration<Print>(skript, "print %objects% [to console]") {
        override fun initialize(skript: Skript, matchResult: MatchResult): Print {
            return Print(matchResult.expressions[0] as Expression<Any>)
        }
    }

    override fun execute() {
        for (obj in objects.get()!!)
            println(obj)
    }
}