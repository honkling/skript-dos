package me.honkling.skript.common

import me.honkling.skript.common.extension.Extension

class Skript {
    val extensions = mutableMapOf<String, Extension>()
    val extension = registerExtension(Extension("skript"))

    init {
        val types = extension.typeRegistry
        types.register<String>("string", "strings", "str", "strs")
        types.register<Int>("integer", "integers", "int", "ints")
        types.register<Boolean>("boolean", "booleans", "bool", "bools")
        types.register<Number>("number", "numbers", "num", "nums")
        types.register<Any>("object", "objects")
    }

    fun registerExtension(extension: Extension): Extension {
        val id = extension.id

        if (id in extensions)
            throw IllegalStateException("Already registered extension with id '${extension.id}'")

        extensions[id] = extension
        return extension
    }
}