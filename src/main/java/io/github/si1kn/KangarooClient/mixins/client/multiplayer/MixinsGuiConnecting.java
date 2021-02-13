package io.github.si1kn.KangarooClient.mixins.client.multiplayer;

import io.github.si1kn.KangarooClient.mods.internal.DiscordRP;
import net.minecraft.client.multiplayer.GuiConnecting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiConnecting.class)
public class MixinsGuiConnecting {


    @Inject(method = "connect", at = @At("HEAD"))
    private void connect(String ip, int port, CallbackInfo ci) {
        DiscordRP.playingOnServer(ip);
    }
}
