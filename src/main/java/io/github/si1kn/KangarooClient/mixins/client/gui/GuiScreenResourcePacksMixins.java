package io.github.si1kn.KangarooClient.mixins.client.gui;

import io.github.si1kn.KangarooClient.mods.ModManager;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreenResourcePacks.class)
public class GuiScreenResourcePacksMixins {

    @Inject(method = "markChanged", at = @At("HEAD"))
    private void markChanged(CallbackInfo ci){
        ModManager.getPackDisplayMod().updateAndLoadTexture();
    }

}
