package me.honkling.skript.common.type

import kotlin.reflect.KClass

class TypeRegistry {
    val types = mutableMapOf<String, Type<*>>()
    val classesToIds = mutableMapOf<KClass<*>, String>()

    inline fun <reified T : Any> register(vararg ids: String): Type<T> {
        val type = Type<T>(ids.toList())

        for (id in ids)
            types[id] = type

        classesToIds[T::class] = ids[0]
        return type
    }

    fun type(id: String) = types[id]
    fun type(clazz: KClass<*>) = types[classesToIds[clazz]]
}