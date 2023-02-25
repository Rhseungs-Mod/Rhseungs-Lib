package net.rhseung.rhseungslib.things

import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.rhseung.rhseungslib.registration.RegistryHelper

class BasicItem constructor(
	val path: String,
	val group: ItemGroup? = null
) : Item(Settings()) {
	
	init {
		RegistryHelper.registerItem(path, this, group)
	}
}