package me.honkling.skript.common.expression.primitive

import me.honkling.skript.common.syntax.Expression
import kotlin.Boolean

class PrimitiveBoolean(private val value: Boolean) : Expression<kotlin.Boolean>() {
    override fun isSingle() = true
    override fun get(): Array<Boolean> {
        return arrayOf(value)
    }
}