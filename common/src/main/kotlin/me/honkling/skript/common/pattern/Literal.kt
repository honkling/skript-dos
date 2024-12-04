package me.honkling.skript.common.pattern

import me.honkling.skript.common.Skript

data class Literal(val value: String) : PatternElement {
    override fun matchPattern(input: String, skript: Skript): MatchResult? {
        if (!input.startsWith(value))
            return null

        return MatchResult(value.length)
    }

    override fun getKeywords(): List<String> {
        return listOf(value)
    }
}