package net.rhseung.rhseungslib.datagen

import net.minecraft.data.server.recipe.ComplexRecipeJsonBuilder
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.RecipeProvider
import net.minecraft.item.ItemConvertible
import net.minecraft.recipe.CraftingRecipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.util.Identifier
import net.rhseung.rhseungslib.api.classes.Type
import java.util.function.Consumer
import kotlin.reflect.KClass

class RecipeManager (
	val modId: String,
	val recipeProvider: Consumer<RecipeJsonProvider>
) {
	fun <T : CraftingRecipe> generateComplexRecipe(
		serializer: RecipeSerializer<T>,
		path: String
	) {
		ComplexRecipeJsonBuilder.create(serializer).offerTo(recipeProvider, Identifier(modId, path).toString())
	}
	
	fun generateShapelessRecipe(
		input: ItemConvertible,
		output: ItemConvertible,
		outputCount: Int = 1
	) {
		RecipeProvider.offerShapelessRecipe(recipeProvider, output, input, "", outputCount)
	}
}