package me.honkling.skript.common.pattern

import me.honkling.skript.common.Skript
import me.honkling.skript.common.lib.isSpace

private typealias ParseMatch<T> = Pair<Int, T>
private val startCharacters = listOf('(', '[', '<', '|', '%')
private val endCharacters = listOf(')', ']', '>', '|')

fun parseGroup(skript: Skript, pattern: String): ParseMatch<Group> {
    val elements = mutableListOf<PatternElement>()
    var index = 0

    while (index < pattern.length && pattern[index] !in endCharacters) {
        val input = pattern.substring(index)
        val (size, element) = when (input[0]) {
            ' ' -> {
                index++
                continue
            }
            '(' -> parseChoice(skript, input, false)
            '[' -> parseChoice(skript, input, true)
            '<' -> parseRegex(input)
            '%' -> parseExpression(skript, input)
            else -> parseLiteral(input)
        }

        index += size
        elements += element
    }

    return index to Group(elements)
}

private fun parseExpression(skript: Skript, input: String): ParseMatch<Expression> {
    val typeBuilder = StringBuilder()
    var index = 0

    if (input[index++] != '%')
        throw IllegalArgumentException("Expected '%' when parsing '$input'")

    while (index < input.length && input[index] != '%')
        typeBuilder.append(input[index++])

    if (input[index++] != '%')
        throw IllegalArgumentException("Expected '%' when parsing '$input'")

    val typeName = typeBuilder.toString()
    val type = skript.extensions.values.firstNotNullOfOrNull { it.typeRegistry.type(typeName) }
        ?: throw IllegalArgumentException("Unrecognized type '$typeName'")

    return index to Expression(type)
}

private fun parseRegex(input: String): ParseMatch<RegEx> {
    val pattern = StringBuilder("^")
    var index = 0

    if (input[index++] != '<')
        throw IllegalArgumentException("Expected '<' when parsing '$input'")

    while (index < input.length && input[index] != '>') {
        if (input[index] == '\\') {
            pattern.append(input[index + 1])
            index += 2
            continue
        }

        pattern.append(input[index++])
    }

    if (index >= input.length)
        throw IllegalArgumentException("Unexpected end of input when parsing '$input'")

    if (input[index++] != '>')
        throw IllegalArgumentException("Expected '>' when parsing '$input'")

    return index to RegEx(Regex(pattern.toString()))
}

private fun parseChoice(skript: Skript, input: String, isOption: Boolean): ParseMatch<Choice> {
    val (open, close) = if (isOption) '[' to ']' else '(' to ')'
    val choices = mutableListOf<Group>()
    var index = 0

    if (input[index++] != open)
        throw IllegalArgumentException("Expected '$open' when parsing '$input'")

    while (index < input.length && input[index] != close) {
        val (size, choice) = parseGroup(skript, input.substring(index))
        choices += choice
        index += size

        if (input[index] == '|')
            index++
        else if (input[index] != close)
            throw IllegalArgumentException("Expected '$close' or '|' when parsing '${input.substring(index)}'")
    }

    if (index >= input.length)
        throw IllegalArgumentException("Unexpected end of input when parsing '$input'")

    if (input[index++] != close)
        throw IllegalArgumentException("Expected '$close' when parsing '$input'")

    return index to Choice(choices, isOption)
}

private fun parseLiteral(input: String): ParseMatch<Literal> {
    val builder = StringBuilder()
    var index = 0

    while (index < input.length && input[index] !in startCharacters && input[index] !in endCharacters)
        builder.append(input[index++])

    while (builder.last().isSpace())
        builder.setLength(builder.length - 1)

    val value = builder.toString()
    return value.length to Literal(value)
}