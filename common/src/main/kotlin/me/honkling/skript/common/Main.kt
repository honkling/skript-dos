package me.honkling.skript.common

import me.honkling.skript.common.effect.Print
import me.honkling.skript.common.parser.Parser
import me.honkling.skript.common.section.Test
import me.honkling.skript.common.structure.ScriptLoad
import me.honkling.skript.common.syntax.Structure

fun main() {
    val skript = Skript()
    skript.extension.syntaxRegistry.register(Print.Registration(skript))
    skript.extension.syntaxRegistry.register(ScriptLoad.Registration(skript))
    skript.extension.syntaxRegistry.register(Test.Registration(skript))

    val then = System.currentTimeMillis()
    val parser = Parser(skript, """
        on script load:
            test section: # abc
                print "hello world"
    """.trimIndent())

    val parseResult = parser.parse()
    val now = System.currentTimeMillis()
    println("Parsed in ${now - then}ms")

    parseResult.logger.entries.forEach {
        println("${it.level.name.lowercase()}: ${it.message}")
    }

    parseResult.structures.forEach(Structure::load)
}