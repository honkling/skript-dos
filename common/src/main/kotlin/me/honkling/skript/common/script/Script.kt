package me.honkling.skript.common.script

import me.honkling.skript.common.Skript
import me.honkling.skript.common.parser.ParseResult
import me.honkling.skript.common.parser.Parser

class Script(private val skript: Skript, val input: String) {
    fun load(): ParseResult {
        val parser = Parser(skript, input)
        return parser.parse()
    }
}