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
		val EMPTY = minecraftID("item/")
		val GENERATED = minecraftID("item/generated")
		val HANDHELD = minecraftID("item/handheld")
		val HANDHELD_ROD = minecraftID("item/handheld_rod")
		val TEMPLATE_SHULKER_BOX = minecraftID("item/template_shulker_box")
		val TEMPLATE_BED = minecraftID("item/template_bed")
		val TEMPLATE_BANNER = minecraftID("item/template_banner")
		val TEMPLATE_SKULL = minecraftID("item/template_skull")
	}
	
	private fun BiConsumer<Identifier, Supplier<JsonElement>>.upload(builder: ItemModelBuilder): Identifier {
		this.accept(builder.model.id, Supplier {
			val jsonObject = JsonObject()
			
			jsonObject.addProperty("parent", builder.model.parent.toString())
			
			if (builder.model.textures.isNotEmpty()) {
				val textureJsonObject = JsonObject()
				builder.model.textures.forEach { (textureKey: String, textureValue: String) ->
					textureJsonObject.addProperty(textureKey, textureValue)
				}
				jsonObject.add("textures", textureJsonObject)
			}
			
			if (builder.overrides.isNotEmpty()) {
				val overrideJsonArray = JsonArray()
				builder.overrides.forEach { override ->
					val eachOverride = JsonObject()
					
					val eachPredicate = JsonObject()
					override.predicates.forEach {
						eachPredicate.addProperty(it.key.toString(), it.value)
					}
					
					eachOverride.add("predicate", eachPredicate)
					eachOverride.addProperty("model", override.model.toString())
					
					overrideJsonArray.add(eachOverride)
				}
				jsonObject.add("overrides", overrideJsonArray)
			}
			
			jsonObject
		})
		
		return builder.model.id
	}
	
	fun generate(builder: ItemModelBuilder) {
		val modelCollector = modelGenerator.writer
		
		modelCollector.upload(builder)
		builder.overrides.forEach { override ->
			modelCollector.upload(builder {
				model {
					id { override.model.id }
					parent { override.model.parent }
					textures {
						from(override.model.textures) {
							key { it.key }
							value { it.value }
						}
					}
				}
			})
		}
	}
	
	fun <T : Item> generateItem(
		item: T,
		parent: Identifier = Parents.GENERATED,
	) {
		val identifier = RegistryHelper.getId(item)
		
		this.generate(builder {
			model {
				id { identifier }
				parent { parent }
				textures {
					texture {
						value { identifier.path }
					}
				}
			}
		})
	}
	
	fun builder(lambda: ItemModelBuilder.() -> Unit): ItemModelBuilder {
		return ItemModelBuilder().apply(lambda)
	}
	
	class ItemModelBuilder {
		lateinit var model: Model
		var overrides = mutableListOf<Override>()
		
		fun model(lambda: ModelBuilder.() -> Unit): ItemModelBuilder {
			val model = ModelBuilder().apply(lambda).build()
			if (model.parent == Parents.EMPTY)
				model.parent = Parents.GENERATED
			
			this.model = model
			return this
		}
		
		fun overrides(lambda: OverrideListBuilder.() -> Unit): ItemModelBuilder {
			val overrides = OverrideListBuilder().apply(lambda).build()
			overrides.forEach {
				if (it.model.parent == Parents.EMPTY) it.model.parent = model.parent
			}
			
			this.overrides = overrides
			return this
		}
	}
	
	data class Model(val id: Identifier, var parent: Identifier, val textures: MutableList<Texture>)
	class ModelBuilder {
		lateinit var identifier: Identifier
		var parent = Parents.EMPTY
		val textures = mutableListOf<Texture>()
		
		fun id(lambda: () -> Identifier): ModelBuilder {
			this.identifier = lambda()
			return this
		}
		
		fun parent(lambda: () -> Identifier): ModelBuilder {
			this.parent = lambda()
			return this
		}
		
		fun textures(lambda: TextureListBuilder.() -> Unit): ModelBuilder {
			this.textures.addAll(TextureListBuilder().apply(lambda).build())
			return this
		}
		
		fun build() = Model(identifier, parent, textures)
	}
	
	data class Texture(var key: String, val value: String)
	class TextureBuilder {
		var key: String = ""
		var value: String = ""
		
		fun key(lambda: () -> String): TextureBuilder {
			this.key = lambda()
			return this
		}
		
		fun value(lambda: () -> String): TextureBuilder {
			this.value = lambda()
			return this
		}
		
		fun build() = Texture(key, value)
	}
	class TextureListBuilder {
		private val textureList = mutableListOf<Texture>()
		
		fun texture(lambda: TextureBuilder.() -> Unit): TextureListBuilder {
			val texture = TextureBuilder().apply(lambda).build()
			if (texture.key.isBlank())
				texture.key = "layer${textureList.count()}"
			
			textureList.add(texture)
			return this
		}
		
		fun <T : Any> from(collection: Collection<T>, lambda: TextureBuilder.(it: T) -> Unit): TextureListBuilder {
			collection.forEach {
				val texture = TextureBuilder().apply { lambda(it) }.build()
				if (texture.key.isBlank())
					texture.key = "layer${textureList.count()}"
				
				textureList.add(texture)
			}
			return this
		}
		
		fun build() = textureList
	}
	
	data class Override(val predicates: MutableMap<Identifier, Number>, val model: Model)
	class OverrideBuilder {
		var predicates = mutableMapOf<Identifier, Number>()
		lateinit var model: Model
		
		fun predicate(lambda: () -> Pair<Identifier, Number>): OverrideBuilder {
			this.predicates[lambda().first] = lambda().second
			return this
		}
		
		fun model(lambda: ModelBuilder.() -> Unit): OverrideBuilder {
			this.model = ModelBuilder().apply(lambda).build()
			return this
		}
		
		fun build() = Override(predicates, model)
	}
	class OverrideListBuilder {
		private val overrideList = mutableListOf<Override>()
		
		fun override(lambda: OverrideBuilder.() -> Unit): OverrideListBuilder {
			overrideList.add(OverrideBuilder().apply(lambda).build())
			return this
		}
		
		fun build() = overrideList
	}
}