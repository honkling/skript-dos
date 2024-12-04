package me.honkling.skript.common.pattern

import me.honkling.skript.common.Skript
import me.honkling.skript.common.syntax.Expression as SyntaxExpression
import kotlin.text.MatchResult as RegexMatch

data class Group(val elements: List<PatternElement>) : PatternElement {
    override fun matchPattern(input: String, skript: Skript): MatchResult? {
        val expressions = mutableListOf<SyntaxExpression<*>>()
        val regexes = mutableListOf<RegexMatch>()
        var index = 0

        for (element in elements) {
            while (index < input.length && input[index] == ' ')
                index++

            val current = input.substring(index)

            if (current.startsWith("#")) {
                val toLine = input.indexOf('\n')
                index += if (toLine == -1) current.length else toLine + 1
                continue
            }

            val matchResult = element.matchPattern(current, skript)
                ?: return null

            index += matchResult.size

            regexes.addAll(matchResult.regexes)
            expressions.addAll(matchResult.expressions)
        }

        return MatchResult(index, regexes, expressions)
    }

    override fun getKeywords(): List<String> {
        return elements
            .map(PatternElement::getKeywords)
            .flatten()
    }
}