package net.rhseung.rhseungslib.datagen.tag

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.block.Block
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

abstract class AbstractBlockTagProvider (
	val output: FabricDataOutput,
	val registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>?
) : FabricTagProvider<Block>(output, RegistryKeys.BLOCK, registriesFuture) {
	
	override fun configure(arg: RegistryWrapper.WrapperLookup) {
		val handler = BlockTagHandler(output.modId, arg)
		register(handler)
	}
	
	open fun register(handler: BlockTagHandler) {
	
	}
}