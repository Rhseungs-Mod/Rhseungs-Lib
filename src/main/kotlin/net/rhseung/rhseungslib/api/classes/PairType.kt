package net.rhseung.rhseungslib.api.classes

data class PairType<T : Any, U : Any>(
	val basicType: Type<out T>,
	val instanceType: Type<out U>
)