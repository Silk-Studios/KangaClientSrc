package io.github.si1kn.KangarooClient.keybinds;

import com.google.common.eventbus.Subscribe;
import io.github.si1kn.KangarooClient.KangaClient;
import io.github.si1kn.KangarooClient.events.KeyPressEvent;
import io.github.si1kn.KangarooClient.events.KeyReleaseEvent;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.ArrayUtils;

import java.util.TreeMap;

public class KeyBindHandler {

    public final TreeMap<String, Keybind> keybinds = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public KeyBindHandler() {
        KangaClient.getEventBus().register(this);
    }

    @Subscribe
    public void onKeyPress(KeyPressEvent event) {
        if (Minecraft.getMinecraft().inGameHasFocus && Minecraft.getMinecraft().currentScreen == null) {
            for (Keybind bind : this.keybinds.values()) {
                if (event.getKey() == bind.getKeyCode()) {
                    bind.onPress();
                    bind.setWasPressed(true);
                }
            }
        }
    }

    @Subscribe
    public void onKeyRelease(KeyReleaseEvent event) {
        if (Minecraft.getMinecraft().inGameHasFocus && Minecraft.getMinecraft().currentScreen == null) {
            for (Keybind bind : this.keybinds.values()) {
                if (event.getKey() == bind.getKeyCode()) {
                    bind.onRelease();
                    bind.setWasPressed(false);
                }
            }
        }
    }

    public void registerKeybinding(Keybind bind) {
        this.keybinds.put(bind.getDescription(), bind);
        Minecraft.getMinecraft().gameSettings.keyBindings = ArrayUtils
                .add(Minecraft.getMinecraft().gameSettings.keyBindings, bind);

    }

}
