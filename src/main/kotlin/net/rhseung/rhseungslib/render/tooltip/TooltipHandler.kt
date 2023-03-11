package net.rhseung.rhseungslib.render.tooltip

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.tooltip.TooltipComponent
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.render.item.ItemRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.rhseung.rhseungslib.api.TextUtils.toText
import net.rhseung.rhseungslib.api.classes.Type
import net.rhseung.rhseungslib.api.collection.InstancePair
import net.rhseung.rhseungslib.api.collection.PixelSize
import net.rhseung.rhseungslib.api.collection.PixelSize.Companion.CONTENT
import net.rhseung.rhseungslib.api.collection.PixelSize.Companion.SPACE
import net.rhseung.rhseungslib.api.math.Math.ceil
import net.rhseung.rhseungslib.api.math.Math.floor
import net.rhseung.rhseungslib.api.math.Point3D
import net.rhseung.rhseungslib.render.tooltip.component.AbstractTooltipComponent.Icon
import net.rhseung.rhseungslib.render.tooltip.component.AbstractTooltipComponent.Icon.Companion.ICON_SIZE
import kotlin.reflect.KClass

class TooltipHandler(
	private val tooltipLines: List<InstancePair<Screen, List<Line>>>,
) {
	/**
	 * set tooltip component height using [TooltipBuilder]
	 *
	 * @param screen screen that tooltip component showed in
	 */
	fun getHeight(
		screen: Screen?
	): Int {
		val lines = tooltipLines.find { it.first.isInstance(screen) }?.second
		
		return if (lines != null) (lines.count()) * (CONTENT.height + SPACE.height) - SPACE.height else 0
	}
	
	/**
	 * set tooltip component width using [TooltipBuilder]
	 * 
	 * @param textRenderer text renderer from [TooltipComponent.getWidth]
	 * @param screen screen that tooltip component showed in
	 */
	fun getWidth(
		textRenderer: TextRenderer,
		screen: Screen?,
	): Int {
		val lines = tooltipLines.find { it.first.isInstance(screen) }?.second
		
		return lines?.maxBy { it.width(textRenderer) }?.width(textRenderer) ?: 0
	}
	
	/**
	 * draw tooltip component using [TooltipBuilder]
	 * 
	 * @param textRenderer text renderer from [TooltipComponent.drawText]
	 * @param itemRenderer item renderer from [TooltipComponent.drawText]
	 * @param matrices matrix stack from [TooltipComponent.drawText]
	 * @param initialPos initial position of x, y, z from [TooltipComponent.drawText]
	 * @param screen screen that tooltip component showed in
	 */
	fun draw(
		textRenderer: TextRenderer,
		itemRenderer: ItemRenderer,
		matrices: MatrixStack,
		initialPos: Point3D<Int>,
		screen: Screen?,
	) {
		val pos = initialPos
		
		val lines = tooltipLines.find { it.first.isInstance(screen) }?.second?.forEach { line ->
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
		object KEY {
			val SHIFT = { Screen.hasShiftDown() }
			val CTRL = { Screen.hasControlDown() }
			val ALT = { Screen.hasAltDown() }
		}
		
		/**
		 * example code
		 * ```
		 * val tooltip = tooltip {
		 *     which(EnchantmentScreen::class) {
		 *         line {
		 * 	        + "Toughness: "
		 * 			+ Icon.TOUGHNESS(data.toughness)
		 * 	        + TAB
		 * 	        + Icon.DURABILITY
		 * 	        + SPACE
		 * 	        + "{${data.currentDurability}}{/${data.maxDurability}}".toText(Color.WHITE, Color.GRAY)
		 *         }
		 *         line {}
		 *         line(KEY.SHIFT) {
		 * 	        + Icon.ENCHANTED
		 * 	        + SPACE
		 * 	        + data.enchantability
		 *         }
		 *     }
		 * }
		 */
		fun tooltip(lambda: TooltipBuilder.() -> Unit): TooltipHandler {
			return TooltipBuilder().apply(lambda).build()
		}
		
		/**
		 * builder for tooltip
		 */
		class TooltipBuilder {
			private val tooltips
				= mutableListOf<InstancePair<Screen, List<Line>>>()
			
			/**
			 * To always be displayed regardless of the [screen], [screen] will be `Screen::class`
			 *
			 * @param screen screen that tooltip component showed in
			 * @param lambda enter [EachTooltipBuilder]
			 */
			fun which(
				screen: KClass<out Screen>,
				lambda: EachTooltipBuilder.() -> Unit,
			): TooltipBuilder {
				tooltips.add(Type(screen) to EachTooltipBuilder().apply(lambda).build())
				return this
			}
			
			fun build() = TooltipHandler(tooltips)
		}
		
		/**
		 * builder for each tooltip
		 */
		class EachTooltipBuilder {
			private val lines = mutableListOf<Line>()
			
			/**
			 * @param lambda enter [LineBuilder]
			 */
			fun line(
				lambda: LineBuilder.() -> Unit
			): EachTooltipBuilder {
				lines.add(LineBuilder().apply(lambda).build())
				return this
			}
			
			/**
			 * @param key key press event condition, using [KEY]
			 * @param lambda enter [LineBuilder]
			 */
			fun line(
				key: () -> Boolean,
				lambda: LineBuilder.() -> Unit,
			): EachTooltipBuilder {
				val optionalLine = LineBuilder().apply(lambda).build()
				optionalLine.condition = key
				
				lines.add(optionalLine)
				return this
			}
			
			fun build() = lines
		}
		
		class LineBuilder(val elements: MutableList<Element> = mutableListOf()) {
			operator fun Icon.unaryPlus(): LineBuilder {
				elements.add(IconElement(this, this.count))
				return LineBuilder(elements)
			}
			
			operator fun Int.unaryPlus(): LineBuilder {
				elements.add(TextElement(this.toText()))
				return LineBuilder(elements)
			}
			
			operator fun Float.unaryPlus(): LineBuilder {
				elements.add(TextElement(this.toText()))
				return LineBuilder(elements)
			}
			
			operator fun Double.unaryPlus(): LineBuilder {
				elements.add(TextElement(this.toText()))
				return LineBuilder(elements)
			}
			
			operator fun String.unaryPlus(): LineBuilder {
				elements.add(TextElement(this.toText()))
				return LineBuilder(elements)
			}
			
			operator fun Text.unaryPlus(): LineBuilder {
				elements.add(TextElement(this))
				return LineBuilder(elements)
			}
			
			operator fun PixelSize<Int>.unaryPlus(): LineBuilder {
				elements.add(SpaceElement(this.width))
				return LineBuilder(elements)
			}
			
			fun build() = Line(elements)
		}
		
		/**
		 * @param elements The elements to add to the line.
		 * @param condition The condition to check.
		 */
		open class Line(open val elements: MutableList<Element>, open var condition: () -> Boolean = { true }) {
			fun width(textRenderer: TextRenderer): Int {
				return elements.sumOf { it.width(textRenderer) }
			}
		}
		
		sealed class Element() {
			open fun width(textRenderer: TextRenderer): Int = 0
		}
		
		/**
		 * @param icon The icon to add to the element.
		 * @param count The number of icons to add.
		 */
		data class IconElement(val icon: Icon, val count: Double = 1.0) : Element() {
			override fun width(textRenderer: TextRenderer): Int {
				return ICON_SIZE.width * ceil(count)
			}
		}
		
		/**
		 * @param text The text to add to the element.
		 */
		data class TextElement(val text: Text) : Element() {
			override fun width(textRenderer: TextRenderer): Int {
				return textRenderer.getWidth(text)
			}
		}
		
		/**
		 * @param gap The gap between the elements.
		 */
		class SpaceElement(val gap: Int) : Element() {
			override fun width(textRenderer: TextRenderer): Int {
				return SPACE.width
			}
		}
	}
}