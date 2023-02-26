package net.rhseung.rhseungslib.registration

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import net.rhseung.rhseungslib.Mod.modID
import kotlin.reflect.KClass

object RegistryHelper {
	
	// Items
	fun <T : Item> register(
		item: T,
		id: Identifier,
		group: ItemGroup? = null,
	): T {
		if (group != null)
			ItemGroupEvents.modifyEntriesEvent(group)
				.register(ItemGroupEvents.ModifyEntries { entries -> entries.add(item) })
		return Registry.register(Registries.ITEM, id, item)
	}
	
	fun <T : Item> getItems(
		clazz: KClass<out T>
	): List<T> {
		return Registries.ITEM.streamEntries().map { it.value() }.filter { clazz.isInstance(it) }.map { it as T }.toList()
	}
	
	fun getId(item: Item): Identifier {
		return Registries.ITEM.getId(item).withPrefixedPath("item/")
	}
	
	// Blocks
	fun <T : Block> register(
		block: T,
		id: Identifier,
		group: ItemGroup? = null,
	): T {
		if (group != null)
			ItemGroupEvents.modifyEntriesEvent(group)
				.register(ItemGroupEvents.ModifyEntries { entries -> entries.add(block) })
		
		Registry.register(Registries.ITEM, id, BlockItem(block, Item.Settings()))
		return Registry.register(Registries.BLOCK, id, block)
	}
	
	fun <T : Block> getBlocks(
		clazz: KClass<out T>
	): List<T> {
		return Registries.BLOCK.streamEntries().map { it.value() }.filter { clazz.isInstance(it) }.map { it as T }.toList()
	}
	
	fun getId(block: Block): Identifier {
		return Registries.BLOCK.getId(block).withPrefixedPath("block/")
	}
	
	// ItemGroups
	fun register(
		id: Identifier,
		icon: ItemConvertible
	): ItemGroup {
		return FabricItemGroup.builder(id).icon { ItemStack(icon) }.build()
	}
	
	fun getId(group: ItemGroup): Identifier {
		return group.id
	}
	
	// Recipes
	fun register(
		id: Identifier,
		recipeType: RecipeType<*>,
		recipeSerializer: RecipeSerializer<*>,
	) {
		Registry.register(Registries.RECIPE_SERIALIZER, id, recipeSerializer)
		Registry.register(Registries.RECIPE_TYPE, id, recipeType)
	}
}