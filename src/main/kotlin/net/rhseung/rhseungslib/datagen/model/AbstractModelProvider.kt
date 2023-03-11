package net.rhseung.rhseungslib.datagen.model

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator
import net.rhseung.rhseungslib.registration.RegistryHelper
import net.rhseung.rhseungslib.things.blocks.BasicBlock
import net.rhseung.rhseungslib.things.blocks.BasicItem

abstract class AbstractModelProvider (
	val output: FabricDataOutput
) : FabricModelProvider(output) {
	
	override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {
		val handler = BlockModelHandler(output.modId, blockStateModelGenerator)
		registerBlock(handler)
	}
	
	open fun registerBlock(handler: BlockModelHandler) {
		RegistryHelper.getBlocks(BasicBlock::class).forEach { handler.generateBlock(it) }
	}
	
	override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {
		val handler = ItemModelHandler(output.modId, itemModelGenerator)
		registerItem(handler)
	}
	
	open fun registerItem(handler: ItemModelHandler) {
		RegistryHelper.getItems(BasicItem::class).forEach { handler.generateItem(it) }
	}
}