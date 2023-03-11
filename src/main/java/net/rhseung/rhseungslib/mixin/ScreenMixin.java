package net.rhseung.rhseungslib.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.rhseung.rhseungslib.render.tooltip.component.AbstractTooltipComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(Screen.class)
public abstract class ScreenMixin {
	@ModifyVariable(
			method = "renderTooltipFromComponents(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;)V",
			at = @At("HEAD"),
			ordinal = 0,
			argsOnly = true
	)
	private List<TooltipComponent> modify_list(List<TooltipComponent> list) {
		var screen = (Screen) (Object) this;

		for (var i = 0; i < list.size(); i++) {
			if (!(list.get(i) instanceof OrderedTextTooltipComponent)) {
				list.set(i, ((AbstractTooltipComponent) list.get(i)).setScreen(screen));
			}
		}

		return list;
	}
}