package net.rhseung.rhseungslib.api.classes

data class PairInstance<T : Any, U : Any>(
	val basicType: Type<out T>,
	val instance: U
)