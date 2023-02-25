package net.rhseung.rhseungslib.things

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemGroups
import net.rhseung.rhseungslib.registration.RegistryHelper

class BasicBlock constructor(
	val path: String,
	val material: Material,
	val group: ItemGroup? = null
) : Block(FabricBlockSettings.of(material)) {
	
	init {
		RegistryHelper.registerBlock(path, this, group)
	}
}
