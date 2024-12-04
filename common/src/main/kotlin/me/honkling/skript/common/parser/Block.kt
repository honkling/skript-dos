package me.honkling.skript.common.parser

import me.honkling.skript.common.syntax.Statement

data class Block(
    val statements: List<Statement>
) {
    fun execute() {
        for (statement in statements)
            statement.execute()
    }
}