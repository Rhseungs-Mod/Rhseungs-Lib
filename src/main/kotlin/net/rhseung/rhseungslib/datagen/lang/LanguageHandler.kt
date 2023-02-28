package net.rhseung.rhseungslib.datagen.lang

import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider.TranslationBuilder
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.rhseung.rhseungslib.api.Text.toDisplayName
import net.rhseung.rhseungslib.registration.RegistryHelper
import net.rhseung.rhseungslib.things.BasicBlock
import net.rhseung.rhseungslib.things.BasicItem

class LanguageHandler(
	val modId: String,
	val translationBuilder: TranslationBuilder
) {
	fun generate(item: BasicItem, name: String = "") {
		translationBuilder.add(item, if (name.isBlank()) item.id.path.toDisplayName() else name)
	}
	
	fun generate(item: Item, name: String = "") {
		translationBuilder.add(item, if (name.isBlank()) RegistryHelper.getId(item).path.toDisplayName() else name)
	}
	
	fun generate(block: BasicBlock, name: String = "") {
		translationBuilder.add(block, if (name.isBlank()) block.id.path.toDisplayName() else name)
	}
	
	fun generate(block: Block, name: String = "") {
		translationBuilder.add(block, if (name.isBlank()) RegistryHelper.getId(block).path.toDisplayName() else name)
	}
	
	fun generate(group: ItemGroup, name: String = "") {
		translationBuilder.add(group, if (name.isBlank()) RegistryHelper.getId(group).path.toDisplayName() else name)
	}
	
}