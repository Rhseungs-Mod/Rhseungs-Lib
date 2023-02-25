package net.rhseung.rhseungslib.registration

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import net.rhseung.rhseungslib.api.Text.modID
import kotlin.reflect.KClass

object RegistryHelper {
	fun <T : Item> registerItem(
		path: String,
		item: T,
		group: ItemGroup? = null,
	): T {
		if (group != null)
			ItemGroupEvents.modifyEntriesEvent(group)
				.register(ItemGroupEvents.ModifyEntries { entries -> entries.add(item) })
		return Registry.register(Registries.ITEM, modID(path), item)
	}
	
	fun <T : Block> registerBlock(
		path: String,
		block: T,
		group: ItemGroup? = null,
	): T {
		if (group != null)
			ItemGroupEvents.modifyEntriesEvent(group)
				.register(ItemGroupEvents.ModifyEntries { entries -> entries.add(block) })
		
		Registry.register(Registries.ITEM, modID(path), BlockItem(block, Item.Settings()))
		return Registry.register(Registries.BLOCK, modID(path), block)
	}
	
	fun <T : Item> getItems(
		clazz: KClass<out T>
	): List<T> {
		return Registries.ITEM.streamEntries().map { it.value() }.filter { clazz.isInstance(it) }.map { it as T }.toList()
	}
	
	fun <T : Block> getBlocks(
		clazz: KClass<out T>
	): List<T> {
		return Registries.BLOCK.streamEntries().map { it.value() }.filter { clazz.isInstance(it) }.map { it as T }.toList()
	}
	
	fun getId(item: Item): Identifier {
		return Registries.ITEM.getId(item).withPrefixedPath("item/")
	}
	
	fun getId(block: Block): Identifier {
		return Registries.BLOCK.getId(block).withPrefixedPath("block/")
	}
}