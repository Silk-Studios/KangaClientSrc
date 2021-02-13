package io.github.si1kn.KangarooClient.mods;

import io.github.si1kn.KangarooClient.KangaClient;
import io.github.si1kn.KangarooClient.mods.hud.*;
import io.github.si1kn.KangarooClient.mods.internal.Fullbright;
import io.github.si1kn.KangarooClient.mods.internal.MotionBlurMod;
import lombok.Getter;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;

public class ModManager {


    @Getter
    private static Fullbright fullBright;

    @Getter
    private static MotionBlurMod motionBlurMod;

    @Getter
    private static final FpsMod fpsMod = new FpsMod();

    @Getter
    private static final CpsMod cpsMod = new CpsMod();

    @Getter
    private static final CoordsMod coordsMod = new CoordsMod();

    @Getter
    private static final PackDisplayMod packDisplayMod = new PackDisplayMod();

    @Getter
    private static final SpotifyMod spotifyMod = new SpotifyMod();


    @Getter
    private static final ArrayList<Module> hudMods = new ArrayList<>();


    public void load() {
        fullBright = new Fullbright();
        motionBlurMod = new MotionBlurMod();

        KangaClient.getInstance().registerMultiple(this.motionBlurMod, this.fullBright);
        for (Module module : hudMods) {
            KangaClient.getEventBus().register(module);
            module.load();
        }
    }


    public static void render() {
        if (Minecraft.getMinecraft().currentScreen == null) {
            for (Module module : hudMods) {
                module.render(module.pos);
            }
        }
    }

    public static boolean isModuleInList(Module module) {
        return hudMods.contains(module);
    }

}
