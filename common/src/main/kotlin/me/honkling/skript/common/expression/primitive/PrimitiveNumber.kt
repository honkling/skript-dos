package me.honkling.skript.common.expression.primitive

import me.honkling.skript.common.syntax.Expression
import kotlin.Number

class PrimitiveNumber(private val value: Number) : Expression<Number>() {
    override fun isSingle() = true
    override fun get(): Array<Number> {
        return arrayOf(value)
    }
}