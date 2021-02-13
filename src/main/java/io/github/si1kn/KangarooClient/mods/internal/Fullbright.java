package io.github.si1kn.KangarooClient.mods.internal;

import com.google.common.eventbus.Subscribe;
import io.github.si1kn.KangarooClient.events.ClientTickEvent;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;

public class Fullbright {

    @Getter
    @Setter
    private boolean toggled;

    private float oldGammaSetting;
    private boolean useOnce = false;

    @Subscribe
    public void tickEvent(ClientTickEvent e) {

        if (!this.useOnce) {
            this.oldGammaSetting = Minecraft.getMinecraft().gameSettings.gammaSetting;
            useOnce = true;
        }
        if (toggled) {
            Minecraft.getMinecraft().gameSettings.gammaSetting = 100000f;
        } else {
            Minecraft.getMinecraft().gameSettings.gammaSetting = oldGammaSetting;
        }
    }
}
