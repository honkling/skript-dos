package me.honkling.skript.common.expression.primitive

import me.honkling.skript.common.syntax.Expression

class PrimitiveInteger(private val value: Int) : Expression<Int>() {
    override fun isSingle() = true
    override fun get(): Array<Int> {
        return arrayOf(value)
    }
}