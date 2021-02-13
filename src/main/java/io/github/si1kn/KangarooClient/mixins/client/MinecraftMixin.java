package io.github.si1kn.KangarooClient.mixins.client;


import io.github.si1kn.KangarooClient.KangaClient;
import io.github.si1kn.KangarooClient.events.ClientTickEvent;
import io.github.si1kn.KangarooClient.events.KeyPressEvent;
import io.github.si1kn.KangarooClient.events.KeyReleaseEvent;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {


    @Inject(method = "startGame", at = @At("HEAD"))
    private void preInit(CallbackInfo ci) throws Throwable {
        KangaClient.getInstance().preInit();
    }

    @Inject(method = "startGame", at = @At("RETURN"))
    private void returnInit(CallbackInfo ci) {
        KangaClient.getInstance().startClient();
    }

    @Inject(method = "runTick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        KangaClient.getEventBus().post(new ClientTickEvent());
    }

    @Inject(method = "shutdown", at = @At("HEAD"))
    private void shutdown(CallbackInfo ci) {
        KangaClient.getInstance().stopClient();
    }


    @Inject(method = "dispatchKeypresses", at = @At(value = "INVOKE_ASSIGN", target = "Lorg/lwjgl/input/Keyboard;" +
            "getEventKeyState()Z"))
    private void runTickKeyboard(CallbackInfo ci) {
        KangaClient.getEventBus()
                .post(Keyboard.getEventKeyState() ? new KeyPressEvent(Keyboard.getEventKey(), Keyboard.isRepeatEvent()) : new KeyReleaseEvent(Keyboard.getEventKey(), Keyboard.isRepeatEvent()));
    }
}
