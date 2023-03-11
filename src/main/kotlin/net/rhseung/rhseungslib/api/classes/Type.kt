package net.rhseung.rhseungslib.api.classes

import kotlin.reflect.KClass
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.primaryConstructor

class Type<T: Any> constructor(
	private val type: KClass<out T>
) {
	val annotations = type.annotations
	val name = type.java.simpleName
	val subclasses = type.nestedClasses
	val companion = type.companionObjectInstance
	
	operator fun invoke(vararg parameters: Any?): T {
		return type.createInstance(*parameters)
	}
	
	fun isInstance(obj: Any?): Boolean {
		return type.isInstance(obj);
	}
	
	companion object {
		fun <T: Any> KClass<T>.createInstance(vararg parameters: Any?): T {
			return this.primaryConstructor?.call(*parameters) ?: error("$this primary constructor is null")
		}
		
		fun <T: Any> KClass<T>.name() = this.java.simpleName
	}
}