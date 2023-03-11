package net.rhseung.rhseungslib.mixin.tooltip;

import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.BundleTooltipData;
import net.minecraft.client.item.TooltipData;
import net.rhseung.rhseungslib.render.tooltip.component.BundleTooltipComponent;
import net.rhseung.rhseungslib.render.tooltip.component.AbstractTooltipComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TooltipComponent.class)
public abstract class ComponentMixin {
    @Inject(
            method = "of(Lnet/minecraft/client/item/TooltipData;)Lnet/minecraft/client/gui/tooltip/TooltipComponent;",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void of_mixin(TooltipData data, CallbackInfoReturnable<AbstractTooltipComponent> cir) {
        AbstractTooltipComponent ret;

        if (data instanceof BundleTooltipData)
            ret = new BundleTooltipComponent((BundleTooltipData) data, null);
        else
            throw new IllegalArgumentException("Unknown TooltipComponent from TooltipComponentMixin");

        cir.setReturnValue(ret);
    }
}
