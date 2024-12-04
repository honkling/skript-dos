package me.honkling.skript.common.parser

import me.honkling.skript.common.Skript
import me.honkling.skript.common.lib.isSpace
import me.honkling.skript.common.pattern.Expression

class Location(
    private val skript: Skript,
    rawInput: String
) {
    val fullInput = scrubComments(rawInput)
    val input get() = fullInput.substring(index)
    val marker get() = "$line:$column"
    private var index = 0
    var line = 1; private set
    var column = 1; private set
    val hasInput get() = index < fullInput.length

    fun consume(size: Int = 1) {
        for (i in 0..<size) {
            val character = fullInput[index++]

            if (character == '\n') {
                line++
                column = 1
            } else {
                column++
            }
        }
    }

    fun skipLine() {
        val newLine = input.indexOf('\n')

        if (newLine == -1) {
            index = fullInput.length + 1
            line++
            column = 1
            return
        }

        index += newLine + 1
        line++
        column = 1
    }

    fun peek() = fullInput[index]

    private fun scrubComments(input: String): String {
        val stringType = skript.extensions.values.firstNotNullOf { it.typeRegistry.type(String::class) }
        val stringExpression = Expression(stringType)
        var scrubbedString = StringBuilder()
        var index = 0

        while (index < input.length) {
            when (input[index]) {
                '\n' -> {
                    val endIndex = scrubbedString.indexOfLast { !it.isSpace() }

                    if (endIndex == -1) {
                        index++
                        continue
                    }

                    scrubbedString.setLength(endIndex + 1)
                }
                '"' -> {
                    val match = stringExpression.matchPattern(input.substring(index), skript)

                    if (match == null) {
                        scrubbedString.append(input[index++])
                        continue
                    }

                    scrubbedString.append(input.substring(index, index + match.size))
                    index += match.size
                    continue
                }
                '#' -> {
                    val endIndex = input.indexOf('\n', index)

                    index = if (endIndex == -1) input.length
                            else endIndex

                    continue
                }
            }

            scrubbedString.append(input[index++])
        }

        return scrubbedString.toString()
    }
}