package net.rhseung.rhseungslib.datagen.lang

import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider.TranslationBuilder
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.rhseung.rhseungslib.api.TextUtils.toDisplayName
import net.rhseung.rhseungslib.registration.RegistryHelper
import net.rhseung.rhseungslib.things.blocks.BasicBlock
import net.rhseung.rhseungslib.things.blocks.BasicItem

/**
 * @param modId mod id
 * @param translationBuilder translation builder
 */
class LanguageHandler(
	val modId: String,
	val translationBuilder: TranslationBuilder
) {
	/**
	 * @param item item to translate
	 * @param name name of the [item] (default is [item]'s registry path)
	 */
	fun generate(item: BasicItem, name: String = "") {
		translationBuilder.add(item, if (name.isBlank()) item.id.path.toDisplayName() else name)
	}
	
	/**
	 * @param item item to translate
	 * @param name name of the [item] (default is [item]'s registry path)
	 */
	fun generate(item: Item, name: String = "") {
		translationBuilder.add(item, if (name.isBlank()) RegistryHelper.getId(item).path.toDisplayName() else name)
	}
	
	/**
	 * @param block block to translate
	 * @param name name of the [block] (default is [block]'s registry path)
	 */
	fun generate(block: BasicBlock, name: String = "") {
		translationBuilder.add(block, if (name.isBlank()) block.id.path.toDisplayName() else name)
	}
	
	/**
	 * @param block block to translate
	 * @param name name of the [block] (default is [block]'s registry path)
	 */
	fun generate(block: Block, name: String = "") {
		translationBuilder.add(block, if (name.isBlank()) RegistryHelper.getId(block).path.toDisplayName() else name)
	}
	
	/**
	 * @param itemGroup item group to translate
	 * @param name name of the [itemGroup] (default is [itemGroup]'s registry path)
	 */
	fun generate(itemGroup: ItemGroup, name: String = "") {
		translationBuilder.add(itemGroup, if (name.isBlank()) RegistryHelper.getId(itemGroup).path.toDisplayName() else name)
	}
}