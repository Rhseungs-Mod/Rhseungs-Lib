package net.rhseung.rhseungslib.api.collection

import net.rhseung.rhseungslib.api.classes.Type

data class Types<T : Any, U : Any>(
	val basicType: Type<out T>,
	val instanceType: Type<out U>
)