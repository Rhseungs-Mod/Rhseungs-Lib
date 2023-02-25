package net.rhseung.rhseungslib.api

import net.rhseung.rhseungslib.api.Utils.createInstance
import kotlin.reflect.KClass

typealias TypePair<T, U> = Pair<Type<out T>, Type<out U>>
typealias InstancePair<T, U> = Pair<Type<out T>, U>

class Type<T: Any> constructor(
	val type: KClass<out T>
) {
	val annotations = type.annotations
	val name = type.java.simpleName
	
	fun call(vararg parameters: Any?): T {
		return type.createInstance(*parameters)
	}
}