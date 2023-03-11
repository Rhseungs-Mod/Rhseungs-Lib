package net.rhseung.rhseungslib.datagen.loottable

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider
import net.rhseung.rhseungslib.registration.RegistryHelper
import net.rhseung.rhseungslib.things.blocks.BasicBlock

abstract class AbstractBlockLootTableProvider (
	val output: FabricDataOutput
) : FabricBlockLootTableProvider(output) {
	
	override fun generate() {
		val handler = BlockLootTableHandler(output.modId, this)
		register(handler)
	}
	
	open fun register(handler: BlockLootTableHandler) {
		RegistryHelper.getBlocks(BasicBlock::class).forEach { handler.simpleDrop(it) }
	}
}