package io.github.si1kn.KangarooClient;

import com.google.common.eventbus.EventBus;
import io.github.si1kn.KangarooClient.keybinds.KeyBindHandler;
import io.github.si1kn.KangarooClient.keybinds.impl.RShiftKeybind;
import io.github.si1kn.KangarooClient.mods.ModManager;
import io.github.si1kn.KangarooClient.mods.internal.DiscordRP;
import io.github.si1kn.KangarooClient.utils.FontRendererUtil;
import io.github.si1kn.KangarooClient.utils.saving.Saving;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import java.awt.*;


@SuppressWarnings("ALL")
public class KangaClient {

    @Getter
    private static final KangaClient instance = new KangaClient();

    @Getter
    private final static EventBus eventBus = new EventBus();

    @Getter
    private final static Logger logger = LogManager.getLogger("KangaLogger");

    @Getter
    private static FontRendererUtil font;

    @Getter
    private final ModManager manager = new ModManager();

    @Getter
    private final String version = "Kangaroo Client: " + 0.1;

    @Getter
    private final DiscordRP rp = new DiscordRP();

    @Getter
    private KeyBindHandler handler;

    private final Saving saveOptions = Saving.getInstance();


    public void preInit() {
        this.handler = new KeyBindHandler();
        rp.start();
    }

    public void startClient() {
        this.handler.registerKeybinding(new RShiftKeybind());
        font = new FontRendererUtil("Segoe UI Light", Font.PLAIN, 20);
        Display.setTitle("Minecraft 1.8.9 / " + getVersion());
        manager.load();
        saveOptions.loadSettings();
    }

    public void stopClient() {
        saveOptions.saveOptions();
        this.rp.stop();
    }


    public void registerMultiple(Object... ob) {
        for (Object obj : ob)
            this.eventBus.register(obj);
    }
}
