package me.honkling.skript.common.logging

data class LogEntry(
    val level: LogLevel,
    val message: String,
    val line: Int,
    val column: Int
)