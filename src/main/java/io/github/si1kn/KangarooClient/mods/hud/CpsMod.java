package io.github.si1kn.KangarooClient.mods.hud;

import io.github.si1kn.KangarooClient.gui.hud.Position;
import io.github.si1kn.KangarooClient.mods.Module;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

public class CpsMod extends Module {
    private final List<Long> clicks = new ArrayList<Long>();
    Minecraft mc = Minecraft.getMinecraft();
    private boolean wasPressed;
    private long lastPressed;


    @Override
    public void render(Position pos) {


        final boolean pressed = Mouse.isButtonDown(0);

        if (pressed != this.wasPressed) {
            this.lastPressed = System.currentTimeMillis() + 10;
            this.wasPressed = pressed;
            if (pressed) {
                this.clicks.add(this.lastPressed);
            }
        }
        mc.fontRendererObj.drawString("Cps: " + getCPS(), (int) pos.getX() + 1, (int) pos.getY() + 1, -1);
    }


    private int getCPS() {
        final long time = System.currentTimeMillis();
        this.clicks.removeIf(aLong -> aLong + 1000 < time);
        return this.clicks.size();
    }


    @Override
    public double getWidth() {
        return mc.fontRendererObj.getStringWidth("Cps: " + getCPS());
    }

    @Override
    public double getHeight() {
        return mc.fontRendererObj.FONT_HEIGHT;
    }
}
