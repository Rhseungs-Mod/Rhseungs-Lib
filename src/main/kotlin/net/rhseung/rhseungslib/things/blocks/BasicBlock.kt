package net.rhseung.rhseungslib.things.blocks

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Material
import net.minecraft.entity.EntityType
import net.minecraft.item.ItemGroup
import net.minecraft.loot.LootTables
import net.minecraft.resource.featuretoggle.FeatureFlag
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import net.rhseung.rhseungslib.registration.RegistryHelper
import net.rhseung.rhseungslib.things.ToolType
import java.util.function.Function
import java.util.function.ToIntFunction

class BasicBlock private constructor(
	val id: Identifier,
	val setting: Settings,
) : Block(setting.settings) {
	
	init {
		RegistryHelper.register(this, id, setting.group)
	}
	
	override fun toString(): String {
		return "BasicBlock(id=$id, settings=$setting)"
	}
	
	companion object {
		fun block(
			modId: String,
			lambda: BlockBuilder.() -> Unit,
		) = BlockBuilder(modId).apply(lambda).build()
	}
	
	class BlockBuilder(val modId: String) {
		var path = ""
		private lateinit var settings: Settings
		
		fun settings(lambda: SettingsBuilder.() -> Unit): BlockBuilder {
			this.settings = SettingsBuilder().apply(lambda).build()
			return this
		}
		
		fun build() = BasicBlock(Identifier(modId, path), settings)
	}
	
	class SettingsBuilder {
		lateinit var material: Material
		var toolType: ToolType = ToolType.ANY
		var toolTier: Int = ToolTier.WOOD
		var group: ItemGroup? = null
		var collidable: Boolean = true
		var soundGroup: BlockSoundGroup = BlockSoundGroup.STONE
		var luminance: ToIntFunction<BlockState> = ToIntFunction { 0 }
		var resistance: Float = 0f
		var hardness: Float = 0f
		var randomTicks: Boolean = false
		var slipperiness: Float = 0.6f
		var velocityMultiplier: Float = 1.0f
		var jumpVelocityMultiplier: Float = 1.0f
		var lootTableId: Identifier = LootTables.EMPTY
		var opaque: Boolean = true
		var isAir: Boolean = false
		var blockBreakParticles: Boolean = true
		var allowsSpawningPredicate: TypedContextPredicate<EntityType<*>> =
			TypedContextPredicate { state, world, pos, type -> state.isSideSolidFullSquare(world, pos, Direction.UP) }
		var solidBlockPredicate: ContextPredicate =
			ContextPredicate { state, world, pos -> state.material.blocksLight() && state.isFullCube(world, pos) }
		var postProcessPredicate: ContextPredicate = ContextPredicate { state, world, pos -> false }
		var emissiveLightingPredicate: ContextPredicate = ContextPredicate { state, world, pos -> false }
		var dynamicBounds: Boolean = false
		var offsetType: Function<BlockState, OffsetType> = Function<BlockState, OffsetType> { state -> OffsetType.NONE }
		var requireFeatures: Array<out FeatureFlag> = arrayOf()
		
		fun build(): Settings {
			var settings = Settings(material, toolType, toolTier).settings
				.collidable(collidable)
				.sounds(soundGroup)
				.luminance(luminance)
				.resistance(resistance)
				.hardness(hardness)
				.slipperiness(slipperiness)
				.velocityMultiplier(velocityMultiplier)
				.jumpVelocityMultiplier(jumpVelocityMultiplier)
				.drops(lootTableId)
				.allowsSpawning(allowsSpawningPredicate)
				.solidBlock(solidBlockPredicate)
				.postProcess(postProcessPredicate)
				.emissiveLighting(emissiveLightingPredicate)
				.offsetType(offsetType)
			
			if (dynamicBounds) settings = settings.dynamicBounds()
			if (requireFeatures.isNotEmpty()) settings = settings.requires(*requireFeatures)
			if (!blockBreakParticles) settings = settings.noBlockBreakParticles()
			if (isAir) settings = settings.air()
			if (!opaque) settings = settings.nonOpaque()
			if (toolType != ToolType.ANY) settings = settings.requiresTool()
			if (randomTicks) settings = settings.ticksRandomly()
			
			return Settings(material, toolType, toolTier, settings, group)
		}
	}
	
	data class Settings(
		val material: Material,
		val toolType: ToolType,
		val toolTier: Int = ToolTier.WOOD,
		val settings: FabricBlockSettings = FabricBlockSettings.of(material),
		val group: ItemGroup? = null,
	)
	
	object ToolTier {
		val WOOD = 0
		val STONE = 1
		val IRON = 2
		val DIAMOND = 3
		val NETHERITE = 4
	}
}
