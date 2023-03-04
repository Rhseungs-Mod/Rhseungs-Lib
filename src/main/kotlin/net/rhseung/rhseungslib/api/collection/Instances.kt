package net.rhseung.rhseungslib.api.collection

import net.rhseung.rhseungslib.api.classes.Type

data class Instances<T : Any, U : Any>(
	val basicType: Type<out T>,
	val instance: U
)