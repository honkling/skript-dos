package me.honkling.skript.common.pattern

import me.honkling.skript.common.Skript

data class Choice(val choices: List<Group>, val isOption: Boolean) : PatternElement {
    override fun matchPattern(input: String, skript: Skript): MatchResult? {
        for (choice in choices) {
            val matchResult = choice.matchPattern(input, skript)
                ?: continue

            return matchResult
        }

        if (isOption && input.substringBefore('\n').isBlank())
            return MatchResult(0)

        return null
    }

    override fun getKeywords(): List<String> {
        if (isOption)
            return emptyList()

        val keywordLists = choices.map(Group::getKeywords)
        val commonKeywords = mutableListOf<String>()

        for (keyword in keywordLists[0]) {
            if (keywordLists.any { keyword !in it })
                continue

            commonKeywords += keyword
        }

        return commonKeywords
    }
}