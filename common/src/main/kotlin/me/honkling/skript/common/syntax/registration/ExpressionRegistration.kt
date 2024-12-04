package me.honkling.skript.common.syntax.registration

import me.honkling.skript.common.Skript
import me.honkling.skript.common.syntax.Expression
import me.honkling.skript.common.type.Type

abstract class ExpressionRegistration<T : Any, E : Expression<T>>(
    skript: Skript,
    vararg patterns: String
) : SyntaxRegistration<E>(skript, *patterns) {
    abstract fun returnType(): Type<T>
}