package net.rhseung.rhseungslib.api

import net.minecraft.util.Identifier

object IdentifierUtils {
	private fun Identifier.withPostfixedPath(path: String): Identifier = this.withPath { it + path }
	
	operator fun Identifier.plus(postfix: String) = this.withPostfixedPath(postfix)
	operator fun String.plus(id: Identifier) = id.withPrefixedPath(this)
}