package me.honkling.skript.common.pattern

import me.honkling.skript.common.Skript
import me.honkling.skript.common.expression.primitive.PrimitiveBoolean
import me.honkling.skript.common.expression.primitive.PrimitiveInteger
import me.honkling.skript.common.expression.primitive.PrimitiveNumber
import me.honkling.skript.common.expression.primitive.PrimitiveString
import me.honkling.skript.common.type.Type

data class Expression(private val type: Type<*>) : PatternElement {
    override fun matchPattern(input: String, skript: Skript): MatchResult? {
        val typeRegistry = skript.extension.typeRegistry
        val objectType = typeRegistry.type(Any::class)

        if (type == objectType || type == typeRegistry.type(String::class)) run {
            val value = StringBuilder()
            var index = 0

            if (input[index++] != '"')
                return@run

            while (index < input.length && input[index] != '"' && input[index] != '\n') {
                if (input[index] == '\\') {
                    index++
                    value.append(input[index++])
                    continue
                }

                value.append(input[index++])
            }

            if (input[index++] != '"')
                return@run

            return MatchResult(
                index,
                expressions = listOf(PrimitiveString(value.toString()))
            )
        }

        if (type == objectType || type == typeRegistry.type(Int::class)) run {
            var value = 0
            var index = 0

            while (index < input.length && input[index].isDigit()) {
                value *= 10
                value += input[index++].digitToInt()
            }

            return MatchResult(
                index,
                expressions = listOf(PrimitiveInteger(value))
            )
        }

        if (type == objectType || type == typeRegistry.type(Number::class)) run {
            var multiplier = 10.0
            var value = 0.0
            var index = 0

            while (index < input.length && (input[index].isDigit() || (multiplier == 10.0 && input[index] == '.'))) {
                if (input[index] == '.') {
                    index++
                    multiplier = 0.1
                    continue
                }

                if (multiplier == 10.0) {
                    value *= multiplier
                    value += input[index++].digitToInt()
                } else {
                    value += input[index++].digitToInt() / multiplier
                    multiplier /= 10
                }
            }

            if (index < input.length && !input[index].isWhitespace())
                return@run

            return MatchResult(
                index,
                expressions = listOf(PrimitiveNumber(value))
            )
        }

        if (type == objectType || type == typeRegistry.type(Boolean::class)) run {
            val value = input.startsWith("true")
            val literal = value.toString()

            if ((!value && !input.startsWith("false")) ||
                (literal.length < input.length && !input[literal.length].isWhitespace()))
                    return@run

            return MatchResult(
                literal.length,
                expressions = listOf(PrimitiveBoolean(value))
            )
        }

        val expressions = skript.extensions.values
            .map { it.syntaxRegistry.expressions }
            .flatten()

        for (expression in expressions) {
            val returnType = expression.returnType()

            if (type != returnType && type != objectType && returnType != objectType)
                continue

            for (pattern in expression.patterns) {
                val keywords = pattern.getKeywords()

                if (keywords.any { it !in input })
                    continue

                val matchResult = pattern.matchPattern(input, skript)
                    ?: continue

                return MatchResult(
                    matchResult.size,
                    expressions = listOf(expression.initialize(skript, matchResult))
                )
            }
        }

        return null
    }

    override fun getKeywords(): List<String> {
        return emptyList()
    }
}