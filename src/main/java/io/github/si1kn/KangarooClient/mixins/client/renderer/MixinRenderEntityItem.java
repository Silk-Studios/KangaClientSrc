package io.github.si1kn.KangarooClient.mixins.client.renderer;


import io.github.si1kn.KangarooClient.mods.internal.ItemPhysic;
import io.github.si1kn.KangarooClient.mods.internal.OldAnimations;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.entity.item.EntityItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderEntityItem.class)
public class MixinRenderEntityItem {

    @Inject(method = "doRender", at = @At("HEAD"), cancellable = true)
    private void doRender(EntityItem entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (OldAnimations.isItemPhysic()) {
            ItemPhysic.doRender(entity, x, y, z);
            ci.cancel();
        }
    }

}
