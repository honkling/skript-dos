package me.honkling.skript.common.syntax

abstract class Section : BlockSyntaxElement() {
    abstract fun walk(): Boolean

    override fun execute() {
        if (walk())
            super.execute()
    }
}