package net.rhseung.rhseungslib.api.collection

import net.rhseung.rhseungslib.api.classes.Type
import kotlin.reflect.KClass

typealias InstancePair<K, V> = Pair<Type<out K>, V>
typealias TypePair<K, V> = Pair<Type<out K>, Type<out V>>

object CollectionUtils {
	
	fun <T> Collection<T>.dropLast(n: Int): Collection<T> {
		require(n >= 0) { "n: $n is less than zero." }
		return this.take((this.count() - n).coerceAtLeast(0))
	}
	
	fun <T : Any> Collection<T>.forBoth(action: (first: T, second: T) -> Unit) {
		require(this.count() >= 2) { "collection: $this, collection must have at least two elements" }
		val i = this.iterator()
		
		var first: T
		var second = i.next()
		
		do {
			first = second
			second = i.next()
			action(first, second)
		} while (i.hasNext())
	}
	
	fun <T : Any> Array<out T>.forBoth(action: (first: T, second: T) -> Unit) {
		require(this.size >= 2) { "array: [${this.joinToString()}], array must have at least two elements" }
		val i = this.iterator()
		
		var first: T
		var second = i.next()
		
		do {
			first = second
			second = i.next()
			action(first, second)
		} while (i.hasNext())
	}
	
	fun <T, K, V> Collection<T>.associateIndexed(action: (index: Int, element: T) -> Pair<K, V>): Map<K, V> {
		val map = mutableMapOf<K, V>()
		this.forEachIndexed { idx, t -> map[action(idx, t).first] = action(idx, t).second }
		return map.toMap()
	}
	
	fun <T : Any, K : Any, V : Any> Array<out T>.associateIndexed(action: (index: Int, element: T) -> Pair<K, V>): Map<K, V> {
		val map = mutableMapOf<K, V>()
		this.forEachIndexed { idx, t -> map[action(idx, t).first] = action(idx, t).second }
		return map.toMap()
	}
}