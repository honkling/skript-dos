package me.honkling.skript.common.parser

import me.honkling.skript.common.logging.Logger
import me.honkling.skript.common.syntax.Structure

data class ParseResult(
    val logger: Logger,
    val structures: List<Structure>
)