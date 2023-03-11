package net.rhseung.rhseungslib.datagen.tag

import net.fabricmc.fabric.impl.datagen.FabricTagBuilder
import net.minecraft.block.Block
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

class BlockTagHandler(
	val modId: String,
	arg: RegistryWrapper.WrapperLookup,
//	abstractBlockTagProvider: AbstractBlockTagProvider
) {
	// todo: add and sub operator from vanilla tag
	// todo: operator overloading
	fun builder(path: String): FabricTagBuilder {
		return AbstractBlockTagProvider().getOrCreateTagBuilder(TagKey.of(RegistryKeys.BLOCK, Identifier(modId, path)))
	}
}
