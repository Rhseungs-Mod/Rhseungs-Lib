package net.rhseung.rhseungslib.api.collection

import net.rhseung.rhseungslib.render.tooltip.component.AbstractTooltipComponent

data class PixelSize<T : Number>(
	val width: T,
	val height: T,
) {
	companion object {
		val CONTENT = PixelSize(0, AbstractTooltipComponent.Icon.ICON_SIZE.height)
		val SPACE = PixelSize(3, 2)
		val TAB = PixelSize(8, 4)
	}
}