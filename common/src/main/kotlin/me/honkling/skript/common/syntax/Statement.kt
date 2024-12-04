package me.honkling.skript.common.syntax

interface Statement : SyntaxElement {
    fun execute()
}