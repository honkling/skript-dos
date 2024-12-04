package me.honkling.skript.common.lib

fun Char.isSpace()
    = isWhitespace() && this != '\r' && this != '\n'