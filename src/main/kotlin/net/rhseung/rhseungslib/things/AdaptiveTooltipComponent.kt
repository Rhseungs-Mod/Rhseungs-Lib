package net.rhseung.rhseungslib.things

import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.tooltip.TooltipComponent
import net.minecraft.client.item.TooltipData
import kotlin.reflect.full.primaryConstructor

abstract class AdaptiveTooltipComponent(
	open val data: TooltipData,
	open val screen: Screen? = null,
	open val index: Int? = null
) : TooltipComponent {
	
	fun set(screen: Screen): AdaptiveTooltipComponent {
		return this::class.primaryConstructor!!.call(data, screen)
	}
}