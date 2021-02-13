package io.github.si1kn.KangarooClient.mods.hud;

import io.github.si1kn.KangarooClient.gui.hud.Position;
import io.github.si1kn.KangarooClient.mods.Module;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.ResourcePackRepository;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.List;

public class PackDisplayMod extends Module {

    @Getter
    @Setter
    private static float s = 0.6F;

    private boolean alreadyExecuted;
    private DynamicTexture texture;


    @Override
    public void render(Position pos) {
        GlStateManager.pushMatrix();
        GL11.glColor3f(1, 1, 1);
        GlStateManager.translate(pos.getX(), pos.getY(), 0);
        GlStateManager.scale(s, s, 1);

        if (!alreadyExecuted) {
            updateAndLoadTexture();
        }
        alreadyExecuted = true;

        Minecraft.getMinecraft().getTextureManager().bindTexture(
                Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("texturepackicon", texture));

        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);

        String s1 = null;
        try {
            s1 = getCurrentPack().getPackName();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Gui.drawRect(32, 0, (int) (getWidth() / s), (int) (getHeight() / s + 1), 2097152000);

        Minecraft.getMinecraft().fontRendererObj.drawString(s1, 38, 12, -1);
        GlStateManager.popMatrix();
    }

    @Override
    public double getWidth() {
        String s1 = null;
        try {
            s1 = getCurrentPack().getPackName();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (int) (45 + Minecraft.getMinecraft().fontRendererObj.getStringWidth(s1) * s);
    }

    @Override
    public double getHeight() {
        return (int) (32 * s);
    }

    private IResourcePack getCurrentPack() throws IllegalAccessException {
        List<ResourcePackRepository.Entry> list = Minecraft.getMinecraft().getResourcePackRepository()
                .getRepositoryEntries();
        if (list.size() > 0) {
            return list.get(0).getResourcePack();
        }

        return (IResourcePack) FieldUtils.readField(Minecraft.getMinecraft(), "mcDefaultResourcePack", true);

    }

    public void updateAndLoadTexture() {
        try {
            texture = new DynamicTexture(getCurrentPack().getPackImage());
        } catch (IOException ex) {
            texture = TextureUtil.missingTexture;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
