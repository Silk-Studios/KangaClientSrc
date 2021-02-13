package io.github.si1kn.KangarooClient.mixins.client.renderer;

import io.github.si1kn.KangarooClient.mods.ModManager;
import io.github.si1kn.KangarooClient.mods.internal.ItemPhysic;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

    @Inject(method = "updateCameraAndRender", at = @At("RETURN"))
    public void updateCameraAndRender(float p_181560_1_, long p_181560_2_, CallbackInfo ci) {
        ModManager.render();
        ItemPhysic.tick();
    }

}
