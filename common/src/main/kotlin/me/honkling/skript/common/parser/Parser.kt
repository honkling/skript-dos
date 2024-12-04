package me.honkling.skript.common.parser

import me.honkling.skript.common.Skript
import me.honkling.skript.common.lib.isSpace
import me.honkling.skript.common.logging.Logger
import me.honkling.skript.common.syntax.Statement
import me.honkling.skript.common.syntax.Structure
import me.honkling.skript.common.syntax.SyntaxElement
import me.honkling.skript.common.syntax.registration.StructureRegistration
import me.honkling.skript.common.syntax.registration.SyntaxRegistration
import me.honkling.skript.common.syntax.registration.SyntaxRegistry
import kotlin.reflect.KProperty1

class Parser(
    private val skript: Skript,
    input: String
) {
    private val location = Location(skript, input)
    val logger = Logger(location)

    fun parse(): ParseResult {
        val structures = mutableListOf<Structure>()

        while (location.hasInput) {
            val structure = parseStructure()

            if (structure == null) {
                location.skipLine()
                skipBlock()
                continue
            }

            val block = parseBlock()

            if (block == null) {
                skipBlock()
                continue
            }

            structure.block = block
            structures += structure
        }

        return ParseResult(logger, structures)
    }

    fun parseStructure() = parseSyntaxElement(
        "Unrecognized structure",
        SyntaxRegistry::structures
    )

    fun parseSection() = parseSyntaxElement(
        "Unrecognized section",
        SyntaxRegistry::sections
    )

    fun parseEffect() = parseSyntaxElement(
        "Unrecognized effect",
        SyntaxRegistry::effects
    )

    fun parseBlock(
        initialType: String = "",
        initialAmount: Int = 0
    ): Block? {
        if (location.peek() == ':')
            location.skipLine()

        val statements = mutableListOf<Statement>()
        var indentationAmount = initialAmount + 1
        val indentationType = initialType.ifEmpty { detectIndentationType() }
            ?: return null

        while (location.hasInput && location.peek().isWhitespace()) {
            if (location.peek() == '\n') {
                location.consume()
                continue
            }

            // Handle indentation
            for (i in 0..<indentationAmount) {
                if (!location.input.startsWith(indentationType)) {
                    if (indentationAmount == 1)
                        break

                    indentationAmount = i
                    break
                }

                location.consume(indentationType.length)
            }

            if (location.peek().isSpace()) {
                errorIndentation(indentationType, indentationAmount)
                location.skipLine()
                continue
            }

            // Parse
            if (location.input.substringBefore("\n").endsWith(":")) {
                // Parsing a section
                val section = parseSection()
                val block = section?.let { parseBlock(indentationType, indentationAmount)  }

                if (section == null || block == null) {
                    skipBlock(indentationType, indentationAmount + 1)
                    continue
                }

                section.block = block
                statements += section
                continue
            }

            val effect = parseEffect()

            if (effect == null) {
                location.skipLine()
                continue
            }

            statements += effect
        }

        return Block(statements)
    }

    private fun errorIndentation(indentationType: String, indentationAmount: Int) {
        val type = indentationType[0]
        val expectedSize = indentationType.length * indentationAmount
        val expected =
            "$expectedSize ${nameIndent(type, expectedSize)}"

        val actualIndentation = detectIndentationType(type)
            ?: return

        val actualType = actualIndentation[0]
        val actualSize = actualIndentation.length + expectedSize
        val actual = "$actualSize ${nameIndent(actualType, actualSize)}"
        logger.error("Improper indentation; expected $expected, found $actual")
    }

    private fun nameIndent(type: Char, amount: Int = 1): String {
        return (if (type == ' ') "space" else "tab") + if (amount == 1) "" else "s"
    }

    private fun detectIndentationType(type: Char = location.peek()): String? {
        val input = location.input
        val endIndex = input.indexOfFirst { it != type }

        if (input[endIndex].isSpace() && input[endIndex] != type) {
            logger.error("Inconsistent indentation type: Only tabs or only spaces can be used, not both.")
            return null
        }

        return input.substring(0, endIndex)
    }

    private fun skipBlock(
        indentationType: String? = null,
        indentationAmount: Int = 0
    ) {
        if (location.peek() == ':')
            location.skipLine()

        while (location.hasInput && if (indentationType == null) location.peek().isSpace() else location.input.startsWith(indentationType.repeat(indentationAmount)))
            location.skipLine()
    }

    private fun <T : SyntaxElement> parseSyntaxElement(message: String?, accessor: KProperty1<SyntaxRegistry, List<SyntaxRegistration<out T>>>): T? {
        val candidates = skript.extensions.values
            .map { accessor.get(it.syntaxRegistry) }
            .flatten()

        for (registration in candidates)
            for (pattern in registration.patterns) {
                val matchResult = pattern.matchPattern(location.input, skript)
                    ?: continue

                location.consume(matchResult.size)
                return registration.initialize(skript, matchResult)
            }

        if (message != null)
            logger.error(message)

        return null
    }
}