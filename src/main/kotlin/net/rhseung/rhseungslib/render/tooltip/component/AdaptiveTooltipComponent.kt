package net.rhseung.rhseungslib.render.tooltip.component

import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.tooltip.TooltipComponent
import net.minecraft.client.item.TooltipData
import net.minecraft.client.render.item.ItemRenderer
import net.minecraft.client.util.math.MatrixStack
import net.rhseung.rhseungslib.Mod.modID
import net.rhseung.rhseungslib.api.math.Point3D
import net.rhseung.rhseungslib.render.tooltip.TooltipHandler
import kotlin.math.floor
import kotlin.reflect.full.primaryConstructor

abstract class AdaptiveTooltipComponent(
	open val data: TooltipData,
	open val screen: Screen? = null
) : TooltipComponent {
	
	open val builder = TooltipHandler()
	
	override fun getHeight(): Int {
		return builder.height()
	}
	
	override fun getWidth(textRenderer: TextRenderer): Int {
		return builder.width(textRenderer)
	}
	
	override fun drawItems(
		textRenderer: TextRenderer,
		x: Int,
		y: Int,
		matrices: MatrixStack,
		itemRenderer: ItemRenderer,
		z: Int
	) {
		builder.build(textRenderer, itemRenderer, matrices, Point3D(x, y, z))
	}
	
	fun setScreen(screen: Screen): AdaptiveTooltipComponent {
		return this::class.primaryConstructor!!.call(data, screen)
	}
	
	// each icon has 9x9 size texture
	enum class Icon(
		val variants: Int = 1
	) {
		ATTACK_DAMAGE,
		ATTACK_SPEED,
		ATTACK_KNOCKBACK,
		DURABILITY,
		FIRE,
		HUNGER(2),
		SATURATION(4),
		PROTECTION,
		TOUGHNESS,
		KNOCKBACK_RESISTANCE,
		LUCK,
		MAX_HEALTH,
		SPEED,
		ENCHANTED,
		POSITIVE,
		NEGATIVE;
		
		val path = modID("textures/gui/icon/${name.lowercase()}")
		
		fun index(ratio: Float): Int {
			return if (ratio == 1.0F) variants - 1
			else floor(ratio * variants).toInt()
		}
		fun u(ratio: Float): Int = index(ratio) * 9
		fun v(ratio: Float): Int = 0
		
		override fun toString(): String {
			return "§§$name§§"
		}
	}
}