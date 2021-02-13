package io.github.si1kn.KangarooClient.gui.hud;

import io.github.si1kn.KangarooClient.mods.ModManager;
import io.github.si1kn.KangarooClient.mods.Module;
import io.github.si1kn.KangarooClient.utils.RenderingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.io.IOException;

public class HudEditor extends GuiScreen {

    private int lastX;
    private int lastY;
    private Module mod = null;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        if (mod != null) {
            mod.pos.x += mouseX - this.lastX;
            mod.pos.y += mouseY - this.lastY;
            this.adjustBounds(mod, mod.pos);
        }

        for (Module mod : ModManager.getHudMods()) {
            mod.render(mod.pos);
            Position pos = mod.pos;
            RenderingUtils.drawRect(pos.getX(), pos.getY(), pos.getX() + mod.getWidth(), pos.getY() + mod.getHeight(), 0x33FFFFFF);
            RenderingUtils.drawHollowRect((int) pos.getX(), (int) pos.getY(), (int) (mod.getWidth()), (int) (mod.getHeight()), -1);
        }

        this.lastX = mouseX;
        this.lastY = mouseY;
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (Module mod : ModManager.getHudMods()) {
            if (isMouseOver(mod, mouseX, mouseY)) {
                this.mod = mod;
                this.lastX = mouseX;
                this.lastY = mouseY;
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.mod = null;
    }

    public boolean isMouseOver(Module module, double mouseX, double mouseY) {
        double minX = module.pos.getX();
        double minY = module.pos.getY();
        double maxX = minX + module.getWidth();
        double maxY = minY + module.getHeight();
        return mouseX > minX && mouseY > minY && mouseX < maxX && mouseY < maxY;
    }

    private void adjustBounds(Module mod, Position pos) {
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        final int screenWidth = res.getScaledWidth();
        final int screenHeight = res.getScaledHeight();
        final int newX = (int) Math.max(3, Math.min(pos.getX(), Math.max(screenWidth - mod.getWidth() - 3, 0)));
        final int newY = (int) Math.max(3, Math.min(pos.getY(), Math.max(screenHeight - mod.getHeight() - 3, 0)));
        pos.setY(newY);
        pos.setX(newX);
    }

}
