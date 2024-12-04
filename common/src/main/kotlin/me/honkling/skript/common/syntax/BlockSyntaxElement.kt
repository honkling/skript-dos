package me.honkling.skript.common.syntax

import me.honkling.skript.common.parser.Block

abstract class BlockSyntaxElement : SyntaxElement, Statement {
    lateinit var block: Block

    override fun execute() {
        block.execute()
    }
}