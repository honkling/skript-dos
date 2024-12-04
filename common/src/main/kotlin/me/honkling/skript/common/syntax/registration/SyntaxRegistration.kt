package me.honkling.skript.common.syntax.registration

import me.honkling.skript.common.Skript
import me.honkling.skript.common.pattern.MatchResult
import me.honkling.skript.common.pattern.parseGroup
import me.honkling.skript.common.syntax.SyntaxElement

abstract class SyntaxRegistration<T : SyntaxElement>(
    private val skript: Skript,
    vararg patterns: String
) {
    val patterns = patterns.map { parseGroup(skript, it).second }

    abstract fun initialize(skript: Skript, matchResult: MatchResult): T
}