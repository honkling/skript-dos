package me.honkling.skript.common.syntax.registration

import me.honkling.skript.common.Skript
import me.honkling.skript.common.parser.Block
import me.honkling.skript.common.pattern.MatchResult
import me.honkling.skript.common.syntax.Structure

abstract class StructureRegistration<T : Structure>(
    skript: Skript,
    vararg patterns: String
) : SyntaxRegistration<T>(skript, *patterns)