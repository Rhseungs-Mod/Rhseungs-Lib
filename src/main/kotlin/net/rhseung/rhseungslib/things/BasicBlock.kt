package net.rhseung.rhseungslib.things

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
import java.util.function.Function
import java.util.function.ToIntFunction

class BasicBlock private constructor(
	val id: Identifier,
	val group: ItemGroup? = null,
	val settings: FabricBlockSettings,
) : Block(settings) {
	
	init {
		RegistryHelper.register(this, id, group)
	}
	
	override fun toString(): String {
		return "BasicBlock(id=$id, group=$group, settings=$settings)"
	}
	
	class BlockBuilder constructor(
		val path: String,
		val group: ItemGroup? = null,
	) {
		private lateinit var settings: FabricBlockSettings
		
		fun settings(
			material: Material,
			collidable: Boolean = true,
			soundGroup: BlockSoundGroup = BlockSoundGroup.STONE,
			luminance: ToIntFunction<BlockState> = ToIntFunction { 0 },
			resistance: Float = 0f,
			hardness: Float = 0f,
			toolRequired: Boolean = false,
			randomTicks: Boolean = false,
			slipperiness: Float = 0.6f,
			velocityMultiplier: Float = 1.0f,
			jumpVelocityMultiplier: Float = 1.0f,
			lootTableId: Identifier = LootTables.EMPTY,
			opaque: Boolean = true,
			isAir: Boolean = false,
			blockBreakParticles: Boolean = true,
			allowsSpawningPredicate: TypedContextPredicate<EntityType<*>> = TypedContextPredicate { state, world, pos, type ->
				state.isSideSolidFullSquare(
					world,
					pos,
					Direction.UP
				)
			},
			solidBlockPredicate: ContextPredicate = ContextPredicate { state, world, pos ->
				state.material.blocksLight() && state.isFullCube(
					world,
					pos
				)
			},
			postProcessPredicate: ContextPredicate = ContextPredicate { state, world, pos -> false },
			emissiveLightingPredicate: ContextPredicate = ContextPredicate { state, world, pos -> false },
			dynamicBounds: Boolean = false,
			offsetType: Function<BlockState, OffsetType> = Function<BlockState, OffsetType> { state -> OffsetType.NONE },
			vararg requireFeatures: FeatureFlag,
		): BlockBuilder {
			this.settings = FabricBlockSettings.of(material).collidable(collidable).sounds(soundGroup)
				.luminance(luminance)
				.resistance(resistance).hardness(hardness)
				.slipperiness(slipperiness).velocityMultiplier(velocityMultiplier)
				.jumpVelocityMultiplier(jumpVelocityMultiplier).drops(lootTableId)
				.allowsSpawning(allowsSpawningPredicate)
				.solidBlock(solidBlockPredicate).postProcess(postProcessPredicate)
				.emissiveLighting(emissiveLightingPredicate).offsetType(offsetType)
			
			if (dynamicBounds) settings = settings.dynamicBounds()
			if (requireFeatures.isNotEmpty()) settings = settings.requires(*requireFeatures)
			if (!blockBreakParticles) settings = settings.noBlockBreakParticles()
			if (isAir) settings = settings.air()
			if (!opaque) settings = settings.nonOpaque()
			if (toolRequired) settings = settings.requiresTool()
			if (randomTicks) settings = settings.ticksRandomly()
			
			return this
		}
		
		fun of(
			modId: String
		): BasicBlock {
			return BasicBlock(Identifier(modId, path), group, settings)
		}
	}
}
