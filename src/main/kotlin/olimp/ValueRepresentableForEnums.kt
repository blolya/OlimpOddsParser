package olimp

import kotlin.reflect.KClass

interface ValueRepresentable<V> {
    val value: V
}

inline fun <reified T, V> enumValue(repr: V): T? where T : Enum<T>, T : ValueRepresentable<V> {
    return enumValue(T::class, repr)
}

fun <T, V> enumValue(cl: KClass<T>, repr: V): T? where T : Enum<T>, T : ValueRepresentable<V> {
    return cl.java.enumConstants.find { it.value == repr }
}