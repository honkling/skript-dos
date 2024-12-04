package me.honkling.skript.common.pattern

import me.honkling.skript.common.syntax.Expression
import kotlin.text.MatchResult

data class MatchResult(
    val size: Int,
    val regexes: List<MatchResult> = emptyList(),
    val expressions: List<Expression<*>> = emptyList()
)