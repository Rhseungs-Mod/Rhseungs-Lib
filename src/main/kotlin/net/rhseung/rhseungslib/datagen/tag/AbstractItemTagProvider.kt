package net.rhseung.rhseungslib.datagen.tag

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

abstract class AbstractItemTagProvider (
	val output: FabricDataOutput,
	val registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>?
) : FabricTagProvider<Item>(output, RegistryKeys.ITEM, registriesFuture) {
	
	override fun configure(arg: RegistryWrapper.WrapperLookup) {
		val handler = ItemTagHandler(output.modId, arg)
		register(handler)
	}
	
	open fun register(handler: ItemTagHandler) {
	
	}
}