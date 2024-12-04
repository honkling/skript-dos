package me.honkling.skript.common.extension

import me.honkling.skript.common.syntax.registration.SyntaxRegistry
import me.honkling.skript.common.type.TypeRegistry

class Extension(val id: String) {
    val syntaxRegistry = SyntaxRegistry()
    val typeRegistry = TypeRegistry()
}