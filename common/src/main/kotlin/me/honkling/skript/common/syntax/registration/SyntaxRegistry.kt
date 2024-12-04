package me.honkling.skript.common.syntax.registration

import me.honkling.skript.common.syntax.*

class SyntaxRegistry {
    val structures = mutableListOf<SyntaxRegistration<out Structure>>()
    val sections = mutableListOf<SyntaxRegistration<out Section>>()
    val effects = mutableListOf<SyntaxRegistration<out Effect>>()
    val expressions = mutableListOf<ExpressionRegistration<*, out Expression<*>>>()

    inline fun <reified T : SyntaxElement> register(registration: SyntaxRegistration<T>) {
        val it = T::class.java

        if (Expression::class.java.isAssignableFrom(it) && registration !is ExpressionRegistration<*, *>)
            throw IllegalArgumentException("Expression cannot be registered with a generic syntax registration")

        when {
            Structure::class.java.isAssignableFrom(it) -> structures.add(registration as SyntaxRegistration<out Structure>)
            Section::class.java.isAssignableFrom(it) -> sections.add(registration as SyntaxRegistration<out Section>)
            Effect::class.java.isAssignableFrom(it) -> effects.add(registration as SyntaxRegistration<out Effect>)
            Expression::class.java.isAssignableFrom(it) -> expressions.add(registration as ExpressionRegistration<*, out Expression<*>>)
            else -> throw IllegalArgumentException("Unknown syntax element type '${T::class.simpleName}'")
        }
    }
}