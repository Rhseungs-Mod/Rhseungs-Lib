package net.rhseung.rhseungslib.things

import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.resource.featuretoggle.FeatureFlag
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
import net.rhseung.rhseungslib.registration.RegistryHelper

class BasicItem private constructor(
	val id: Identifier,
	val group: ItemGroup? = null,
	val settings: Settings,
) : Item(settings) {
	
	init {
		RegistryHelper.register(this, id, group)
	}
	
	override fun toString(): String {
		return "BasicItem(id=$id, group=$group, settings=$settings)"
	}
	
	class ItemBuilder constructor(
		val path: String,
		val group: ItemGroup? = null,
	) {
		private var settings: Settings = Settings()
		
		fun settings(
			maxCount: Int = 64,
			maxDamage: Int = 0,
			recipeRemainder: Item? = null,
			rarity: Rarity = Rarity.COMMON,
			fireproof: Boolean = false,
			vararg requireFeatures: FeatureFlag
		): ItemBuilder {
			this.settings =
				settings.maxCount(maxCount).maxDamage(maxDamage).recipeRemainder(recipeRemainder).rarity(rarity)
			if (requireFeatures.isNotEmpty()) this.settings = settings.requires(*requireFeatures)
			if (fireproof) this.settings = settings.fireproof()
			
			return this
		}
		
		fun of(
			modId: String
		): BasicItem {
			return BasicItem(Identifier(modId, path), group, settings)
		}
	}
}