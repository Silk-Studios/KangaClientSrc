package io.github.si1kn.KangarooClient.mixins.client.gui;

import io.github.si1kn.KangarooClient.gui.MainMenuGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMainMenu.class)
public class GuiMainMenuMixin {
    @Inject(method = "initGui", at = @At("HEAD"))
    public void init(CallbackInfo ci) {
        Minecraft.getMinecraft().displayGuiScreen(new MainMenuGui());
    }
}

