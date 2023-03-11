package net.rhseung.rhseungslib.datagen.recipe

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.item.Items
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.tag.ItemTags
import java.util.function.Consumer

abstract class AbstractRecipeProvider (
	val output: FabricDataOutput
) : FabricRecipeProvider(output) {
	
	override fun generate(exporter: Consumer<RecipeJsonProvider>) {
		val handler = RecipeHandler(output.modId, exporter)
		register(handler)
	}
	
	open fun register(handler: RecipeHandler) {
	}
}