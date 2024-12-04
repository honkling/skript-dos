package me.honkling.skript.common.logging

import me.honkling.skript.common.parser.Location

class Logger(private val location: Location) {
    val entries = mutableListOf<LogEntry>()
    val errors get() = entries
        .filter { it.level == LogLevel.Error }
        .size
    val hasError get() = errors > 0

    fun log(level: LogLevel, message: String) {
        entries += LogEntry(
            level,
            message,
            location.line,
            location.column
        )
    }

    fun info(message: String) = log(LogLevel.Info, message)
    fun warning(message: String) = log(LogLevel.Warning, message)
    fun error(message: String) = log(LogLevel.Error, message)
}