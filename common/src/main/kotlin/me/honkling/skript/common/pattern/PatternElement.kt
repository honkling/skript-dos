package me.honkling.skript.common.pattern

import me.honkling.skript.common.Skript

interface PatternElement {
    fun matchPattern(input: String, skript: Skript): MatchResult?
    fun getKeywords(): List<String>
}