package net.rhseung.rhseungslib.datagen.tag

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.block.Block
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import net.rhseung.rhseungslib.registration.RegistryHelper
import net.rhseung.rhseungslib.things.ToolAction
import net.rhseung.rhseungslib.things.blocks.BasicBlock
import java.util.concurrent.CompletableFuture

abstract class AbstractBlockTagProvider(
	val output: FabricDataOutput,
	val registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>?,
) : FabricTagProvider<Block>(output, RegistryKeys.BLOCK, registriesFuture) {
	
	override fun configure(arg: RegistryWrapper.WrapperLookup) {
		val handler = BlockTagHandler(output.modId, arg)
		
		RegistryHelper.getBlocks(BasicBlock::class).forEach { block ->
			this.getOrCreateTagBuilder(
				TagKey.of(
					RegistryKeys.BLOCK,
					Identifier(output.modId, "tool_tier_${block.setting.toolTier}")
				)
			).add(block)
		}
		
		
		
		ToolAction.SWORD.minableTag.~~~
		ToolAction.PICKAXE.minableTag.~~~
		
		// todo: auto minable tag
		
		register(handler)
	}
	
	open fun register(handler: BlockTagHandler) {
	
	}
}