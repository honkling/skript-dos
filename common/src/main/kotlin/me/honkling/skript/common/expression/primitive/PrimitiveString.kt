package me.honkling.skript.common.expression.primitive

import me.honkling.skript.common.syntax.Expression
import kotlin.String

class PrimitiveString(private val value: String) : Expression<String>() {
    override fun isSingle() = true
    override fun get(): Array<String> {
        return arrayOf(value)
    }
}