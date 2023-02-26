package net.rhseung.rhseungslib.render.tooltip

import net.minecraft.util.Identifier
import net.rhseung.rhseungslib.Mod.modID

enum class Icon constructor(
	vararg variants: String
) {
	PROTECTION,
	TOUGHNESS,
	KNOCKBACK_RESISTANCE,
	ATTACK_DAMAGE,
	ATTACK_SPEED,
	MINING_LEVEL,
	MINING_SPEED,
	DURABILITY,
	HUNGER("_1"),
	SATURATION("_1", "_2", "_3"),
	ENCHANTMENT;
	
	private var variants: List<String> = listOf("") + variants.asList()
	
	fun path(index: Int = 0): Identifier {
		return modID("textures/icon/${name.lowercase() + variants[index]}.png")
	}
}