package net.rhseung.rhseungslib.render.tooltip

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.block.FurnaceBlock
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen
import net.minecraft.client.render.item.ItemRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.rhseung.rhseungslib.api.TextUtils.toText
import net.rhseung.rhseungslib.api.classes.Color
import net.rhseung.rhseungslib.api.collection.PixelSize
import net.rhseung.rhseungslib.api.math.Math.ceil
import net.rhseung.rhseungslib.api.math.Math.floor
import net.rhseung.rhseungslib.api.math.Point3D
import net.rhseung.rhseungslib.render.tooltip.component.AdaptiveTooltipComponent
import net.rhseung.rhseungslib.render.tooltip.component.AdaptiveTooltipComponent.Icon
import net.rhseung.rhseungslib.render.tooltip.component.AdaptiveTooltipComponent.Icon.Companion.ICON_SIZE

class TooltipHandler(
	private val tooltipLines: Map<(Screen?) -> Boolean, List<Line>>,
) {
	fun height(screen: Screen?): Int {
		val lines = tooltipLines[tooltipLines.filter { it.key(screen) }.keys.first { it(screen) }]
		
		return if (lines != null) (lines.count()) * (CONTENT.height + SPACE.height) - SPACE.height else 0
	}
	
	fun width(
		textRenderer: TextRenderer,
		screen: Screen?,
	): Int {
		val lines = tooltipLines[tooltipLines.filter { it.key(screen) }.keys.filter { it(screen) }.first()]
		
		return lines?.maxBy { it.width(textRenderer) }?.width(textRenderer) ?: 0
	}
	
	fun draw(
		textRenderer: TextRenderer,
		itemRenderer: ItemRenderer,
		matrices: MatrixStack,
		initialPos: Point3D<Int>,
		screen: Screen?,
	) {
		val pos = initialPos
		
		tooltipLines[tooltipLines.filter { it.key(screen) }.keys.first { it(screen) }]?.forEach { line ->
			line.elements.forEach { element ->
				when (element) {
					is IconElement  -> {
						RenderSystem.setShaderTexture(0, element.icon.path)
						
						for (i in 0..floor(element.count)) {
							val ratio = (element.count - i).coerceAtMost(1.0)
							DrawableHelper.drawTexture(
								matrices,
								pos.x,
								pos.y - 1,
								pos.z,
								element.icon.u(ratio).toFloat(),
								element.icon.v(ratio).toFloat(),
								ICON_SIZE.width,
								ICON_SIZE.height,
								element.icon.width,
								element.icon.height
							)
							
							pos.x += ICON_SIZE.width
						}
					}
					
					is TextElement  -> {
						matrices.translate(0.0, 0.0, 400.0)
						DrawableHelper.drawTextWithShadow(
							matrices,
							textRenderer,
							element.text,
							pos.x,
							pos.y,
							0xFFFFFF
						)
						matrices.translate(0.0, 0.0, -400.0)
						
						pos.x += textRenderer.getWidth(element.text)
					}
					
					is SpaceElement -> {
						pos.x += element.gap
					}
				}
			}
			pos.y += SPACE.height + CONTENT.height
		}
	}
	
	companion object {
		val CONTENT = PixelSize(0, ICON_SIZE.height)
		val SPACE = PixelSize(3, 2)
		val TAB = PixelSize(8, 4)
		
		/**
		 * Tooltip Using Example
		 * ```
		 * val tooltip = tooltip {
		 * 	now({ it is EnchantmentScreen }) {
		 * 		line {
		 * 			addText { "Toughness: ".toText() }
		 * 			addIcons(data.toughness) { Icon.TOUGHNESS }
		 * 			blank { TAB }
		 * 			addIcon { Icon.DURABILITY }
		 * 			space {}
		 * 			addText { "{${data.currentDurability}}{/${data.maxDurability}}".toText(Color.WHITE, Color.GRAY) }
		 * 		}
		 * 		endl {  }
		 * 		line({ Screen.hasShiftDown() }) {
		 * 			addIcon { Icon.ENCHANTED }
		 * 			space {}
		 * 			addText { "${data.enchantability}".toText() }
		 * 		}
		 *     }
		 * }
		 */
		fun tooltip(lambda: TooltipBuilder.() -> Unit): TooltipHandler {
			return TooltipBuilder().apply(lambda).build()
		}
		
		class TooltipBuilder {
			private val tooltips = mutableMapOf<(Screen?) -> Boolean, List<Line>>()
			
			fun now(
				condition: (Screen?) -> Boolean,
				lambda: NowBuilder.() -> Unit,
			): TooltipBuilder {
				tooltips[condition] = NowBuilder().apply(lambda).build()
				return this
			}
			
			fun build() = TooltipHandler(tooltips)
		}
		
		class NowBuilder {
			private val lines = mutableListOf<Line>()
			
			fun line(lambda: LineBuilder.() -> Unit): NowBuilder {
				lines.add(LineBuilder().apply(lambda).build())
				return this
			}
			
			fun line(
				condition: () -> Boolean,
				lambda: LineBuilder.() -> Unit,
			): NowBuilder {
				val optionalLine = LineBuilder().apply(lambda).build()
				optionalLine.condition = condition
				
				lines.add(optionalLine)
				return this
			}
			
			fun endl(lambda: () -> Unit): NowBuilder {
				lines.add(Line(mutableListOf()))
				return this
			}
			
			fun build() = lines
		}
		
		class LineBuilder {
			private val elements = mutableListOf<Element>()
			
			fun addIcon(lambda: () -> Icon): LineBuilder {
				elements.add(IconElement(lambda()))
				return this
			}
			
			fun addIcons(
				count: Double,
				lambda: () -> Icon,
			): LineBuilder {
				elements.add(IconElement(lambda(), count))
				return this
			}
			
			fun addIcons(
				count: Int,
				lambda: () -> Icon,
			): LineBuilder {
				elements.add(IconElement(lambda(), count.toDouble()))
				return this
			}
			
			fun addText(lambda: () -> Text): LineBuilder {
				elements.add(TextElement(lambda()))
				return this
			}
			
			fun blank(lambda: () -> PixelSize<Int>): LineBuilder {
				elements.add(SpaceElement(lambda().width))
				return this
			}
			
			fun space(lambda: () -> Unit): LineBuilder {
				elements.add(SpaceElement(SPACE.width))
				return this
			}
			
			fun build() = Line(elements)
		}
		
		open class Line(open val elements: MutableList<Element>, open var condition: () -> Boolean = { true }) {
			fun width(textRenderer: TextRenderer): Int {
				return elements.sumOf { it.width(textRenderer) }
			}
		}
		
		sealed class Element() {
			open fun width(textRenderer: TextRenderer): Int = 0
		}
		
		data class IconElement(val icon: Icon, val count: Double = 1.0) : Element() {
			override fun width(textRenderer: TextRenderer): Int {
				return ICON_SIZE.width * ceil(count)
			}
		}
		
		data class TextElement(val text: Text) : Element() {
			override fun width(textRenderer: TextRenderer): Int {
				return textRenderer.getWidth(text)
			}
		}
		
		class SpaceElement(val gap: Int) : Element() {
			override fun width(textRenderer: TextRenderer): Int {
				return SPACE.width
			}
		}
	}
}