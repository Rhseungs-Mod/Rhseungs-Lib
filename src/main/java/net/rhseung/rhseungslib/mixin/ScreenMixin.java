package net.rhseung.rhseungslib.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.server.MinecraftServer;
import net.rhseung.rhseungslib.things.AdaptiveTooltipComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
//		if (list.size() >= 2 && !(list.get(1) instanceof OrderedTextTooltipComponent)) {
//			CompoundTooltipComponent x = (CompoundTooltipComponent) list.remove(1);
//			for (var component : x.getData().getComponents()) {
//				if (x.getIndex() != null)
//					list.add(x.getIndex(), x);
//				else
//					list.add(x);
//			}
////            list.add(x);
////            list.add(0, x);
//		}

		var screen = (Screen) (Object) this;

		for (var i = 0; i < list.size(); i++) {
			if (!(list.get(i) instanceof OrderedTextTooltipComponent)) {
				list.set(i, ((AdaptiveTooltipComponent) list.get(i)).set(screen));
			}
		}

		return list;
	}
}