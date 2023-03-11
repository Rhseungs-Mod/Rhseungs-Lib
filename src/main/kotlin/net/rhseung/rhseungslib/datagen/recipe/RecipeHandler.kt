package net.rhseung.rhseungslib.datagen.recipe

import net.minecraft.advancement.criterion.CriterionConditions
import net.minecraft.block.Block
import net.minecraft.data.server.recipe.*
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder.getItemId
import net.minecraft.data.server.recipe.RecipeProvider.*
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.recipe.CraftingRecipe
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.util.Identifier
import net.rhseung.rhseungslib.api.collection.CollectionUtils.forBoth
import java.util.function.Consumer

class RecipeHandler(
	val modId: String,
	val recipeProvider: Consumer<RecipeJsonProvider>,
) {
	fun <T : CraftingRecipe> generateComplexRecipe(
		serializer: RecipeSerializer<T>,
		path: String,
	) {
		ComplexRecipeJsonBuilder.create(serializer).offerTo(recipeProvider, Identifier(modId, path).toString())
	}
	
	fun generateShapelessRecipe(
		builder: ShapelessRecipeBuilder
	) {
		shapeless {
			group { "group" }
			
			inputs {
				input {
				
				}
			}
			
			output {
			}
		}
		
		
		return builder.build()
		
//		category: RecipeCategory = RecipeCategory.MISC,

//		return ShapelessRecipeBuilder().set(category, recipeProvider)
	}
	
	class ShapelessRecipeBuilder {
		private var ingredients = mutableListOf<Ingredient>()
		private val criterions = mutableListOf<Pair<String, CriterionConditions>>()
		private var group: String? = null
		private var category: RecipeCategory = RecipeCategory.MISC
		private lateinit var recipeProvider: Consumer<RecipeJsonProvider>
		
		fun category(
			category: () -> RecipeCategory
		): ShapelessRecipeBuilder {
			this.category = category()
			return this
		}
		
		fun provider(
			provider: () -> Consumer<RecipeJsonProvider>
		): ShapelessRecipeBuilder {
			this.recipeProvider = provider()
			return this
		}
		
		fun group(
			group: () -> String?,
		): ShapelessRecipeBuilder {
			this.group = group()
			return this
		}
		
		fun inputs(
			lambda: inputListBuilder.() -> Unit
		): ShapelessRecipeBuilder {
			this.ingredients.addAll(inputListBuilder.apply(lambda).build())
			return this
		}
		
		fun output(
			lambda: outputBuilder.() -> Unit
		): ShapelessRecipeBuilder {
		
		}
	}
	
	class inputListBuilder {
		private val inputList = mutableListOf<>
		
		fun input(
			lambda: inputBuilder.() -> Unit
		): inputListBuilder {
		
		}
		
		fun build() = inputList
	}
	
	class inputBuilder {
		fun input(
			vararg input: ItemConvertible,
		): ShapelessRecipeBuilder {
			input.forEach {
				ingredients.add(Ingredient.ofItems(it))
				criterions.add(hasItem(it) to conditionsFromItem(it))
			}
			return this
		}
		
		fun input(
			vararg input: Pair<ItemConvertible, Int>,
		): ShapelessRecipeBuilder {
			input.forEach {
				for (i in 1..it.second)
					ingredients.add(Ingredient.ofItems(it.first))
				criterions.add(hasItem(it.first) to conditionsFromItem(it.first))
			}
			return this
		}
		
		fun input(
			input: TagKey<Item>,
			criterionString: String,
		): ShapelessRecipeBuilder {
			ingredients.add(Ingredient.fromTag(input))
			criterions.add(criterionString to conditionsFromTag(input))
			return this
		}
		
		fun input(
			ingredient: Ingredient,
		): ShapelessRecipeBuilder {
			ingredients.add(ingredient)
			return this
		}
	}
	
	data class Input(val )
	
	fun generateShapedRecipe(
		category: RecipeCategory = RecipeCategory.MISC,
	): ShapedRecipeBuilder {
		return ShapedRecipeBuilder().set(category, recipeProvider)
	}
	
	class ShapedRecipeBuilder {
		private val patterns = mutableListOf<String>()
		private val inputs = mutableListOf<Pair<Char, ItemConvertible>>()
		private var group: String? = null
		private var category: RecipeCategory? = null
		private var recipeProvider: Consumer<RecipeJsonProvider>? = null
		
		fun set(
			category: RecipeCategory,
			recipeProvider: Consumer<RecipeJsonProvider>,
		): ShapedRecipeBuilder {
			this.category = category
			this.recipeProvider = recipeProvider
			return this
		}
		
		fun pattern(
			vararg pattern: String,
		): ShapedRecipeBuilder {
			patterns.addAll(pattern)
			return this
		}
		
		fun group(
			group: String?,
		): ShapedRecipeBuilder {
			this.group = group
			return this
		}
		
		fun input(
			vararg input: Pair<Char, ItemConvertible>,
		): ShapedRecipeBuilder {
			inputs.addAll(input)
			return this
		}
		
		fun output(
			output: ItemConvertible,
			outputCount: Int = 1,
			recipeId: Identifier = getItemId(output),
		) {
			require(category != null && recipeProvider != null) {
				"You must initialize the recipe builder with a category and a recipe provider"
			}
			
			var jsonBuilder = ShapedRecipeJsonBuilder(category, output, outputCount)
			patterns.forEach { jsonBuilder = jsonBuilder.pattern(it) }
			inputs.forEach { jsonBuilder = jsonBuilder.input(it.first, it.second) }
			inputs.forEach {
				jsonBuilder = jsonBuilder.criterion(
					hasItem(it.second),
					conditionsFromItem(it.second)
				)
			}
			jsonBuilder.group(group).offerTo(recipeProvider, recipeId)
		}
	}
	
	fun generateSmeltingRecipe(
		input: ItemConvertible,
		output: ItemConvertible,
		outputExperience: Float,
		smeltingTime: Int,
		group: String? = null,
	) {
		CookingRecipeJsonBuilder.create(
			Ingredient.ofItems(input),
			RecipeCategory.MISC,
			output,
			outputExperience,
			smeltingTime,
			RecipeSerializer.SMELTING
		).group(group)
			.criterion(
				RecipeProvider.hasItem(input),
				RecipeProvider.conditionsFromItem(input)
			)
			.offerTo(
				recipeProvider,
				"smelting/${getItemPath(input)}_to_${getItemPath(output)}"
			)
	}
	
	fun generateBlastingRecipe(
		input: ItemConvertible,
		output: ItemConvertible,
		outputExperience: Float,
		blastingTime: Int,
		group: String? = null,
	) {
		CookingRecipeJsonBuilder.create(
			Ingredient.ofItems(input),
			RecipeCategory.MISC,
			output,
			outputExperience,
			blastingTime,
			RecipeSerializer.BLASTING
		).group(group)
			.criterion(
				RecipeProvider.hasItem(input),
				RecipeProvider.conditionsFromItem(input)
			)
			.offerTo(
				recipeProvider,
				"blasting/${getItemPath(input)}_to_${getItemPath(output)}"
			)
	}
	
	fun generateSmokingRecipe(
		input: ItemConvertible,
		output: ItemConvertible,
		outputExperience: Float,
		smeltingTime: Int,
		group: String? = null,
	) {
		CookingRecipeJsonBuilder.create(
			Ingredient.ofItems(input),
			RecipeCategory.MISC,
			output,
			outputExperience,
			smeltingTime,
			RecipeSerializer.SMOKING
		).group(group)
			.criterion(
				hasItem(input),
				conditionsFromItem(input)
			)
			.offerTo(
				recipeProvider,
				"smoking/${getItemPath(input)}_to_${getItemPath(output)}"
			)
	}
	
	fun generateStoneCutterRecipe(
		input: ItemConvertible,
		output: ItemConvertible,
		outputCount: Int = 1,
		category: RecipeCategory
	) {
		offerStonecuttingRecipe(recipeProvider, category, output, input, outputCount)
	}
	
	fun generateCompactingRecipe(
		input: ItemConvertible,
		output: ItemConvertible,
		reversible: Boolean = true,
	) {
		RecipePatterns.FULL_3X3.set(getCategory(output), recipeProvider).input('#' to input).output(output)
		if (reversible) generateShapelessRecipe(getCategory(input)).input(output to 9).output(input)
	}
	
	fun generate2x2CompactingRecipe(
		input: ItemConvertible,
		output: ItemConvertible,
		reversible: Boolean = true,
	) {
		RecipePatterns.FULL_2X2.set(getCategory(output), recipeProvider).input('#' to input).output(output)
		if (reversible) generateShapelessRecipe(getCategory(input)).input(output to 9).output(input)
	}
	
	//
	fun generateMetalRecipe(
		nugget: Item,
		ingot: Item,
		block: Block
	) {
		listOf(nugget, ingot, block).forBoth { part, compressed ->
			generateCompactingRecipe(part, compressed)
		}
	}
	
	fun generateOreRecipe(
		ingot: Item,
		rawItem: Item,
		rawBlock: Block,
		oreBlock: Block,
		deepslateOreBlock: Block,
		
		outputExperience: Float,
		smeltingTime: Int = 200,
		group: String? = null
	) {
		generateCompactingRecipe(rawItem, rawBlock)
		
		listOf(oreBlock, deepslateOreBlock, rawItem).forEach {
			generateSmeltingRecipe(it, ingot, outputExperience, smeltingTime, group)
			generateBlastingRecipe(it, ingot, outputExperience, smeltingTime / 2, group)
		}
	}
	
	fun getCategory(itemConvertible: ItemConvertible) =
		if (itemConvertible is Block) RecipeCategory.BUILDING_BLOCKS else RecipeCategory.MISC
	
	object RecipePatterns {
		val FULL_3X3 = ShapedRecipeBuilder().pattern(
			"###",
			"###",
			"###"
		)
		val FULL_2X2 = ShapedRecipeBuilder().pattern(
			"##",
			"##"
		)
	}
	
	companion object {
		fun shapeless(lambda: ShapelessRecipeBuilder.() -> Unit): ShapelessRecipeBuilder {
			return ShapelessRecipeBuilder().apply(lambda)
		}
	}
}