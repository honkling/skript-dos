package me.honkling.skript.common.syntax

abstract class Expression<T : Any> : SyntaxElement {
    abstract fun isSingle(): Boolean
    abstract fun get(): Array<T>?
    open fun getSingle() = get()?.get(0)
}