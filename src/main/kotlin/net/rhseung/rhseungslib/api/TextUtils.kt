package net.rhseung.rhseungslib.api

import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.rhseung.rhseungslib.api.classes.Color

object TextUtils {
	fun String.toPathName() = this.lowercase().replace(" ", "_")
	
	fun String.toDisplayName() = this.lowercase().split("/").reversed().map { it.split("_") }.flatten()
		.joinToString(" ") { str -> str.replaceFirstChar { it.titlecase() } }
	
	fun Double.toText(): Text {
		return Text.literal(
			if (this.toInt().toDouble() == this) this.toInt().toString()
			else this.toString()
		)
	}
	
	fun Float.toText(): Text {
		return Text.literal(
			if (this.toInt().toFloat() == this) this.toInt().toString()
			else this.toString()
		)
	}
	
	fun Int.toText(): Text {
		return Text.literal(this.toString())
	}
	
	fun coloring(
		text: String,
		color: Color,
	): MutableText {
		val t = Text.literal(text)
		t.style = t.style.withColor(color.toInt())
		return t
	}
	
	fun String.coloring(
		vararg colors: Color,
		s: String = "<<",
		c: String = ">>",
		tag: String = "<<COLOR>>",
	): MutableText {
		val newText = this.replace("\\$s(.*?)$c".toRegex(), "\n$tag$1\n")
		val arr = newText.split("\n").filter { e -> e != "" }
		var ret = Text.literal("")
		var idx = 0
		
		for (e in arr) {
			ret = if (e.startsWith('#')) {
				ret.append(
					coloring(
						e.substring(1),
						if (colors.count() > idx) colors[idx++] else Color.WHITE
					)
				)
			}
			else {
				ret.append(e)
			}
		}
		
		return ret
	}
	
	fun String.toText(vararg formattingColors: Color): Text {
		return if (formattingColors.isEmpty()) Text.literal(this)
		else this.coloring(*formattingColors)
	}
}