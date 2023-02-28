package net.rhseung.rhseungslib.datagen.model

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.rhseung.rhseungslib.Mod.minecraftID
import net.rhseung.rhseungslib.registration.RegistryHelper
import java.util.function.BiConsumer
import java.util.function.Supplier

class ItemModelHandler (
	val modId: String,
	val modelGenerator: ItemModelGenerator
) {
	object Parents {
		val GENERATED = minecraftID("item/generated")
		val HANDHELD = minecraftID("item/handheld")
		val HANDHELD_ROD = minecraftID("item/handheld_rod")
		val TEMPLATE_SHULKER_BOX = minecraftID("item/template_shulker_box")
		val TEMPLATE_BED = minecraftID("item/template_bed")
		val TEMPLATE_BANNER = minecraftID("item/template_banner")
		val TEMPLATE_SKULL = minecraftID("item/template_skull")
	}
	
	private fun BiConsumer<Identifier, Supplier<JsonElement>>.upload(builder: ItemModelBuilder): Identifier {
		this.accept(builder.id, Supplier {
			val jsonObject = JsonObject()
			
			jsonObject.addProperty("parent", builder.parent.toString())
			
			if (builder.textures.isNotEmpty()) {
				val textureJsonObject = JsonObject()
				builder.textures.forEach { (textureKey: String, textureId: Identifier) ->
					textureJsonObject.addProperty(textureKey, textureId.toString())
				}
				jsonObject.add("textures", textureJsonObject)
			}
			
			if (builder.overrides.isNotEmpty()) {
				val overrideJsonArray = JsonArray()
				builder.overrides.forEach { overrideBuilder ->
					val eachOverride = JsonObject()
					
					val eachPredicate = JsonObject()
					overrideBuilder.predicates.forEach {
						eachPredicate.addProperty(it.key.toString(), it.value)
					}
					
					eachOverride.add("predicate", eachPredicate)
					eachOverride.addProperty("model", overrideBuilder.model.toString())
					
					overrideJsonArray.add(eachOverride)
				}
				jsonObject.add("overrides", overrideJsonArray)
			}
			
			jsonObject
		})
		
		return builder.id
	}
	
	fun generate(builder: ItemModelBuilder) {
		val modelCollector = modelGenerator.writer
		
		modelCollector.upload(builder)
		builder.overrides.forEach { override ->
			modelCollector.upload(
				ItemModelBuilder(override.model)
					.setParent(builder.parent)
					.setTexture(override.textures)
			)
		}
	}
	
	class ItemModelBuilder constructor(val id: Identifier) {
		var parent = Parents.GENERATED
		var textures = mutableMapOf<String, Identifier>()
		var overrides = mutableListOf<OverrideBuilder>()
		
		fun setParent(parent: Identifier): ItemModelBuilder {
			this.parent = parent
			return this
		}
		
		fun setTexture(textures: MutableMap<String, Identifier>): ItemModelBuilder {
			this.textures = textures
			return this
		}
		
		fun addTexture(
			name: String,
			texture: String,
			prefix: String = "item/",
			postfix: String = "",
		): ItemModelBuilder {
			this.textures[name] = Identifier(id.namespace, "$prefix$texture$postfix")
			return this
		}
		
		fun addTexture(
			texture: String,
			prefix: String = "item/",
			postfix: String = "",
		): ItemModelBuilder {
			this.textures["layer${this.textures.count()}"] = Identifier(id.namespace, "$prefix$texture$postfix")
			return this
		}
		
		fun <T : Any> addTextures(
			values: Collection<T>,
			prefix: String = "item/",
			postfix: String = "",
			action: (T) -> String,
		): ItemModelBuilder {
			values.map(action)
				.forEach { texture ->
					this.textures["layer${this.textures.count()}"] = Identifier(id.namespace, "$prefix$texture$postfix")
				}
			return this
		}
		
		fun addOverride(): OverrideBuilder {
			return OverrideBuilder(this)
		}
		
		class OverrideBuilder(val itemModelBuilder: ItemModelBuilder) {
			var model = Identifier(itemModelBuilder.id.namespace, "item/")
			var predicates = mutableMapOf<Identifier, Number>()
			var textures = mutableMapOf<String, Identifier>()
			
			fun addPredicate(
				id: Identifier,
				value: Number,
			): OverrideBuilder {
				this.predicates[id] = value
				return this
			}
			
			fun setModel(
				model: String,
				postfix: String = "",
			): OverrideBuilder {
				this.model = Identifier(this.model.namespace, model).withPath { it + postfix }
				return this
			}
			
			fun setModel(
				model: Identifier,
				postfix: String = "",
			): OverrideBuilder {
				this.model = model.withPath { it + postfix }
				return this
			}
			
			fun addTexture(
				texture: String,
				prefix: String = "item/",
				postfix: String = "",
			): OverrideBuilder {
				this.textures["layer${this.textures.count()}"] = Identifier(this.model.namespace, "$prefix$texture$postfix")
				return this
			}
			
			fun <T : Any> addTextures(
				values: Collection<T>,
				prefix: String = "item/",
				postfix: String = "",
				action: (T) -> String,
			): OverrideBuilder {
				values.map(action)
					.forEach { texture ->
						this.textures["layer${this.textures.count()}"] = Identifier(this.model.namespace, "$prefix$texture$postfix")
					}
				return this
			}
			
			fun end(): ItemModelBuilder {
				itemModelBuilder.overrides.add(this)
				return itemModelBuilder
			}
		}
	}
	
	fun <T : Item> generateItem(
		item: T,
		parent: Identifier = Parents.GENERATED,
	) {
		val identifier = RegistryHelper.getId(item)
		this.generate(
			ItemModelBuilder(identifier)
				.setParent(parent)
				.addTexture(identifier.path, prefix = "")
		)
	}
}