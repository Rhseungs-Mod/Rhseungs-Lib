package net.rhseung.rhseungslib.datagen.loottable

import net.minecraft.block.Block
import net.minecraft.data.server.loottable.BlockLootTableGenerator
import net.minecraft.enchantment.Enchantments
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.loot.LootPool
import net.minecraft.loot.LootTable
import net.minecraft.loot.condition.LootCondition
import net.minecraft.loot.condition.MatchToolLootCondition
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.loot.function.ApplyBonusLootFunction
import net.minecraft.loot.function.SetCountLootFunction
import net.minecraft.loot.provider.number.ConstantLootNumberProvider
import net.minecraft.loot.provider.number.UniformLootNumberProvider
import net.minecraft.predicate.NumberRange
import net.minecraft.predicate.item.EnchantmentPredicate
import net.minecraft.predicate.item.ItemPredicate

class BlockLootTableHandler (
	val modId: String,
	val lootTableGenerator: BlockLootTableGenerator
) {
	fun simpleDrop(
		block: Block,
		drop: ItemConvertible,
	) {
		lootTableGenerator.addDrop(block, drop)
	}
	
	fun simpleDrop(block: Block) {
		this.simpleDrop(block, block)
	}
	
	fun conditionallyDrop(
		block: Block,
		drop: ItemConvertible,
		vararg conditions: LootCondition.Builder,
	) {
		val condition = conditions.toSet().reduce { acc, condition -> acc.or(condition) }
		
		lootTableGenerator.addDrop(
			block, LootTable.builder().pool(
				LootPool.builder().conditionally(condition)
					.rolls(ConstantLootNumberProvider.create(1.0f)).with(ItemEntry.builder(drop))
			)
		)
	}
	
	fun silkTouchDrop(
		block: Block,
		drop: ItemConvertible,
	) {
		this.conditionallyDrop(block, drop, Condition.WITH_SILK_TOUCH)
	}
	
	fun shearDrop(
		block: Block,
		drop: ItemConvertible,
	) {
		this.conditionallyDrop(block, drop, Condition.WITH_SILK_TOUCH)
	}
	
	fun silkTouchOrShearDrop(
		block: Block,
		drop: ItemConvertible,
	) {
		this.conditionallyDrop(block, drop, Condition.WITH_SILK_TOUCH, Condition.WITH_SHEARS)
	}
	
	fun silkTouchDrop(block: Block) {
		this.silkTouchDrop(block, block)
	}
	
	fun shearDrop(block: Block) {
		this.shearDrop(block, block)
	}
	
	fun silkTouchOrShearDrop(block: Block) {
		this.silkTouchOrShearDrop(block, block)
	}
	
	fun oreDrop(
		oreBlock: Block,
		oreItem: Item,
		dropCount: IntRange = 1..1,
	) {
		lootTableGenerator.addDrop(
			oreBlock, BlockLootTableGenerator.dropsWithSilkTouch(
				oreBlock,
				lootTableGenerator.applyExplosionDecay(
					oreBlock, ItemEntry.builder(oreItem)
						.apply(
							SetCountLootFunction.builder(
								UniformLootNumberProvider.create(
									dropCount.first.toFloat(),
									dropCount.last.toFloat()
								)
							)
						)
						.apply(ApplyBonusLootFunction.uniformBonusCount(Enchantments.FORTUNE))
				)
			)
		)
	}
	
	object DropCount {
		val REDSTONE = 4..5
		val LAPIS_LAZULI = 4..9
		val COPPER = 2..5
	}
	
	object Condition {
		val WITH_SILK_TOUCH: LootCondition.Builder = MatchToolLootCondition.builder(
			ItemPredicate.Builder.create()
				.enchantment(EnchantmentPredicate(Enchantments.SILK_TOUCH, NumberRange.IntRange.atLeast(1)))
		)
		val WITH_SHEARS: LootCondition.Builder = MatchToolLootCondition.builder(
			ItemPredicate.Builder.create().items(*arrayOf<ItemConvertible>(Items.SHEARS))
		)
	}
}