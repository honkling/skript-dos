package me.honkling.skript.common.pattern

import me.honkling.skript.common.Skript

data class RegEx(val regex: Regex) : PatternElement {
    override fun matchPattern(input: String, skript: Skript): MatchResult? {
        val match = regex.find(input)
            ?: return null

        return MatchResult(
            match.value.length,
            regexes = listOf(match)
        )
    }

    override fun getKeywords(): List<String> {
        return emptyList()
    }
}