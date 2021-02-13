package io.github.si1kn.KangarooClient.mods.hud;

import io.github.si1kn.KangarooClient.gui.hud.Position;
import io.github.si1kn.KangarooClient.mods.Module;
import net.minecraft.client.Minecraft;

public class FpsMod extends Module {


    @Override
    public void render(Position pos) {
        Minecraft.getMinecraft().fontRendererObj.drawString("FPS: " + Minecraft.getDebugFPS(), (int) pos.getX(), (int) pos.getY(), -1);
    }

    @Override
    public double getWidth() {
        return Minecraft.getMinecraft().fontRendererObj.getStringWidth("FPS: " + Minecraft.getDebugFPS() + 10);
    }

    @Override
    public double getHeight() {
        return 20;
    }
}
