package io.github.si1kn.KangarooClient.mixins.client.gui;

import io.github.si1kn.KangarooClient.utils.saving.Saving;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreen.class)
public class GuiScreenMixin {

    @Inject(method = "onGuiClosed", at = @At("HEAD"))
    private void onGuiClosed(CallbackInfo callbackInfo) {
        Saving.getInstance().saveOptions();
    }
}
