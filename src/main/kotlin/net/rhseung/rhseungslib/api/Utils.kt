package net.rhseung.rhseungslib.api

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object Utils {
	fun <T, K, V> Collection<T>.associateIndexed(action: (index: Int, element: T) -> Pair<K, V>): Map<K, V> {
		val map = mutableMapOf<K, V>()
		this.forEachIndexed { idx, t -> map[action(idx, t).first] = action(idx, t).second }
		return map.toMap()
	}
	
	fun <T: Any> KClass<T>.createInstance(vararg parameters: Any?): T {
		return this.primaryConstructor?.call(*parameters) ?: error("$this primary constructor is null")
	}
	
	fun <T: Any> KClass<T>.name(): String {
		return this.java.simpleName
	}
}