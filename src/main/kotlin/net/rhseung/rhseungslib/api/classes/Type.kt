package net.rhseung.rhseungslib.api.classes

import net.rhseung.rhseungslib.api.Utils.createInstance
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObjectInstance

class Type<T: Any> constructor(
	val type: KClass<out T>
) {
	val annotations = type.annotations
	val name = type.java.simpleName
	val subclasses = type.nestedClasses
	val companion = type.companionObjectInstance
	
	fun call(vararg parameters: Any?): T {
		return type.createInstance(*parameters)
	}
}