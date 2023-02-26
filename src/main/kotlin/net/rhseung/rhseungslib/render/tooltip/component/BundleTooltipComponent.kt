package net.rhseung.rhseungslib.render.tooltip.component

import com.mojang.blaze3d.systems.RenderSystem
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.item.BundleTooltipData
import net.minecraft.client.render.item.ItemRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.collection.DefaultedList
import net.rhseung.rhseungslib.Mod.minecraftID
import net.rhseung.rhseungslib.things.AdaptiveTooltipComponent
import kotlin.math.ceil
import kotlin.math.sqrt

class BundleTooltipComponent constructor(
	override val data: BundleTooltipData,
	override val screen: Screen? = null
) : AdaptiveTooltipComponent(data, screen) {
	private val texture = minecraftID("textures/gui/container/bundle.png")
	private var inventory = data.inventory
	private var occupancy = data.bundleOccupancy
	
	private val field_32381 = 4
	private val field_32382 = 1
	
	override fun getHeight(): Int {
		return getRows() * HEIGHT_PER_ROW + GAP_BETWEEN_SLOT + 4
	}
	
	override fun getWidth(textRenderer: TextRenderer?): Int {
		return getColumns() * WIDTH_PER_COLUMN + GAP_BETWEEN_SLOT
	}
	
	override fun drawItems(
		textRenderer: TextRenderer,
		x0: Int,
		y0: Int,
		matrices: MatrixStack,
		itemRenderer: ItemRenderer,
		z0: Int,
	) {
		val x = x0
		val y = y0
		val z = z0
		
		val i = getColumns()
		val j = getRows()
		val bl = occupancy >= 64
		var k = 0
		for (l in 0 until j) {
			for (m in 0 until i) {
				val n = x + m * WIDTH_PER_COLUMN + 1
				val o = y + l * HEIGHT_PER_ROW + 1
				drawSlot(n, o, k++, bl, textRenderer, matrices, itemRenderer, z)
			}
		}
		drawOutline(x, y, i, j, matrices, z)
	}
	
	private fun drawSlot(
		x: Int,
		y: Int,
		index: Int,
		shouldBlock: Boolean,
		textRenderer: TextRenderer,
		matrices: MatrixStack,
		itemRenderer: ItemRenderer,
		z: Int,
	) {
		if (index >= inventory!!.size) {
			draw(matrices, x, y, z, if (shouldBlock) Sprite.BLOCKED_SLOT else Sprite.SLOT)
		} else {
			val itemStack = inventory[index]
			draw(matrices, x, y, z, Sprite.SLOT)
			itemRenderer.renderInGuiWithOverrides(itemStack, x + 1, y + 1, index)
			itemRenderer.renderGuiItemOverlay(textRenderer, itemStack, x + 1, y + 1)
			if (index == 0) {
				HandledScreen.drawSlotHighlight(matrices, x + 1, y + 1, z)
			}
		}
	}
	
	private fun drawOutline(
		x: Int,
		y: Int,
		columns: Int,
		rows: Int,
		matrices: MatrixStack,
		z: Int,
	) {
		draw(matrices, x, y, z, Sprite.BORDER_CORNER_TOP)
		draw(matrices, x + columns * WIDTH_PER_COLUMN + 1, y, z, Sprite.BORDER_CORNER_TOP)
		var i = 0
		while (i < columns) {
			draw(matrices, x + 1 + i * WIDTH_PER_COLUMN, y, z, Sprite.BORDER_HORIZONTAL_TOP)
			draw(matrices, x + 1 + i * WIDTH_PER_COLUMN, y + rows * HEIGHT_PER_ROW, z, Sprite.BORDER_HORIZONTAL_BOTTOM)
			++i
		}
		i = 0
		while (i < rows) {
			draw(matrices, x, y + i * HEIGHT_PER_ROW + 1, z, Sprite.BORDER_VERTICAL)
			draw(matrices, x + columns * WIDTH_PER_COLUMN + 1, y + i * HEIGHT_PER_ROW + 1, z, Sprite.BORDER_VERTICAL)
			++i
		}
		draw(matrices, x, y + rows * HEIGHT_PER_ROW, z, Sprite.BORDER_CORNER_BOTTOM)
		draw(matrices, x + columns * WIDTH_PER_COLUMN + 1, y + rows * HEIGHT_PER_ROW, z, Sprite.BORDER_CORNER_BOTTOM)
	}
	
	private fun draw(
		matrices: MatrixStack,
		x: Int,
		y: Int,
		z: Int,
		sprite: Sprite,
	) {
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
		RenderSystem.setShaderTexture(0, texture)
		DrawableHelper.drawTexture(
			matrices,
			x,
			y,
			z,
			sprite.u.toFloat(),
			sprite.v.toFloat(),
			sprite.width,
			sprite.height,
			TEXTURE_WIDTH,
			TEXTURE_HEIGHT
		)
	}
	
	private fun getColumns(): Int {
		return GAP_BETWEEN_SLOT.coerceAtLeast(ceil(sqrt(inventory!!.size.toDouble() + 1.0)).toInt())
	}
	
	private fun getRows(): Int {
		return ceil((inventory!!.size.toDouble() + 1.0) / getColumns().toDouble()).toInt()
	}
	
	@Environment(EnvType.CLIENT)
	private enum class Sprite(
		val u: Int,
		val v: Int,
		val width: Int,
		val height: Int
	) {
		SLOT(0, 0, WIDTH_PER_COLUMN, HEIGHT_PER_ROW),
		BLOCKED_SLOT(0, 40, WIDTH_PER_COLUMN, HEIGHT_PER_ROW),
		BORDER_VERTICAL(0, WIDTH_PER_COLUMN, 1, HEIGHT_PER_ROW),
		BORDER_HORIZONTAL_TOP(0, HEIGHT_PER_ROW, WIDTH_PER_COLUMN, 1),
		BORDER_HORIZONTAL_BOTTOM(0, 60, WIDTH_PER_COLUMN, 1),
		BORDER_CORNER_TOP(0, HEIGHT_PER_ROW, 1, 1),
		BORDER_CORNER_BOTTOM(0, 60, 1, 1);
	}
	
	companion object {
		const val WIDTH_PER_COLUMN = 18
		const val HEIGHT_PER_ROW = 20
		const val TEXTURE_WIDTH = 128
		const val TEXTURE_HEIGHT = 128
		const val GAP_BETWEEN_SLOT = 2
	}
}