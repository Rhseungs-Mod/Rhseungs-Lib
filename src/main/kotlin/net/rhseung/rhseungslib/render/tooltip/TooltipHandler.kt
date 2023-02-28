package net.rhseung.rhseungslib.render.tooltip

import net.minecraft.client.font.TextRenderer
import net.minecraft.client.render.item.ItemRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.rhseung.rhseungslib.api.classes.Color
import net.rhseung.rhseungslib.api.math.Point3D


class TooltipHandler {
	
	class TooltipBuilder {
		val lines = mutableListOf<TooltipLine>()
		
		fun line(str: String, vararg colors: Color): TooltipBuilder {
			val text = str.
			
			lines.add(TooltipRequiredLine(text))
			return this
		}
		
		fun optionalLine(condition: () -> Boolean, str: String, vararg colors: Color): TooltipBuilder {
			lines.add(TooltipOptionalLine(condition, text))
			return this
		}
		
		fun height(): Int {
		
		}
		
		fun width(textRenderer: TextRenderer): Int {
		
		}
		
		fun build(
			textRenderer: TextRenderer,
			itemRenderer: ItemRenderer,
			matrices: MatrixStack,
			startPos: Point3D<Int>,
		): TooltipHandler {
		
		}
		
		open class TooltipLine(open val text: Text)
		class TooltipRequiredLine(override val text: Text): TooltipLine(text)
		class TooltipOptionalLine(val condition: () -> Boolean, override val text: Text): TooltipLine(text)
	}
}