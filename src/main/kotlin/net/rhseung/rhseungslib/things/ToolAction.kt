package net.rhseung.rhseungslib.things

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.item.Items
import net.minecraft.item.SwordItem
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.ActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object ToolAction {
	val SWORD = ToolAction(
		canMine = Items.WOODEN_SWORD::canMine,
		postHit = Items.WOODEN_SWORD::postHit,
		postMine = Items.WOODEN_SWORD::postMine,
		getMiningSpeedMultiplier = Items.WOODEN_SWORD::getMiningSpeedMultiplier
	)
	val PICKAXE = ToolAction(
		minableTag = BlockTags.PICKAXE_MINEABLE
	)
	val AXE = ToolAction(
		minableTag = BlockTags.AXE_MINEABLE,
		useOnBlock = Items.WOODEN_AXE::useOnBlock   // fixme: STRIPPED_BLOCKS 가 정의안됨;
	)
	val SHOVEL = ToolAction(
		minableTag = BlockTags.SHOVEL_MINEABLE,
		useOnBlock = Items.WOODEN_SHOVEL::useOnBlock
	)
	val HOE = ToolAction(
		minableTag = BlockTags.HOE_MINEABLE,
		useOnBlock = Items.WOODEN_HOE::useOnBlock
	)
	
	// TODO. tool action includes:
	//  - useOnBlock (axe -> strip, shovel -> pathway, hoe -> plow)
	//  - canMine
	//  - postHit
	//  - postMine
	class ToolAction(
		val minableTag: TagKey<Block>? = null,
		val useOnBlock: ((context: ItemUsageContext) -> ActionResult)? = null,
		val canMine: ((state: BlockState, world: World, pos: BlockPos, miner: PlayerEntity) -> Boolean)? = null,
		val postHit: ((stack: ItemStack, target: LivingEntity, attacker: LivingEntity) -> Boolean)? = null,
		val postMine: ((stack: ItemStack, world: World, state: BlockState, pos: BlockPos, miner: LivingEntity) -> Boolean)? = null,
		val getMiningSpeedMultiplier: ((stack: ItemStack, state: BlockState) -> Float)? = null
	)
}