package me.honkling.skript.common

import me.honkling.skript.common.effect.Print
import me.honkling.skript.common.effect.Shutdown
import me.honkling.skript.common.parser.Parser
import me.honkling.skript.common.section.Chance
import me.honkling.skript.common.section.Loop
import me.honkling.skript.common.section.Test
import me.honkling.skript.common.structure.ScriptLoad
import me.honkling.skript.common.syntax.Structure

fun main() {
    val skript = Skript()
    val syntaxRegistry = skript.extension.syntaxRegistry

    // Effects
    syntaxRegistry.register(Print.Registration(skript))
    syntaxRegistry.register(Shutdown.Registration(skript))

    // Structures
    syntaxRegistry.register(ScriptLoad.Registration(skript))

    // Sections
    syntaxRegistry.register(Chance.Registration(skript))
    syntaxRegistry.register(Loop.Registration(skript))
    syntaxRegistry.register(Test.Registration(skript))

    val then = System.currentTimeMillis()
    val parser = Parser(skript, """
        on script load:
            test section: # abc
                print "hello world"
                loop 5 times:
                    chance of 50:
                        print "50% chance"
    """.trimIndent())

    val parseResult = parser.parse()
    val now = System.currentTimeMillis()
    println("Parsed in ${now - then}ms")

    parseResult.logger.entries.forEach {
        println("${it.level.name.lowercase()}: ${it.message}")
    }

    parseResult.structures.forEach(Structure::load)
}