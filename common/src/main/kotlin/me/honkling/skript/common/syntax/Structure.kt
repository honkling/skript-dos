package me.honkling.skript.common.syntax

abstract class Structure : BlockSyntaxElement() {
    abstract fun load()
}