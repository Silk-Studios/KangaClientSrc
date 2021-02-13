package io.github.si1kn.KangarooClient.gui;

import io.github.si1kn.KangarooClient.KangaClient;
import io.github.si1kn.KangarooClient.gui.button.KangaButton;
import io.github.si1kn.KangarooClient.mods.internal.DiscordRP;
import io.github.si1kn.KangarooClient.utils.RenderingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.temporal.ValueRange;

public class MainMenuGui extends GuiScreen {

    private final ResourceLocation background = new ResourceLocation("kangaClient/gui/mainMenu/mc.png");
    private int lastX = 0, lastY = 0;
    private float moveX = 0, moveY = 0;

    @Override
    public void initGui() {
        DiscordRP.createNewPresence("Sitting in Main Menu", "Playing: 1.8.9 / " + KangaClient.getInstance().getVersion());
        buttonList.clear();
        int a = this.height / 4 + 48;
        this.drawMinecraftButtons(a);
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.disableAlpha();
        this.renderBackground(mouseX, mouseY);

        GlStateManager.pushMatrix();
        String s1 = "Copyright Mojang AB. Do not distribute!";
        String s2 = "Logged in as: " + mc.getSession().getUsername();
        String s3 = "Minecraft: 1.8.9";

        RenderingUtils.drawOutlinedRect(0, 50, 100, 400, 0x90000000, 0xff000000);

        RenderingUtils.drawString(s1, (this.width - KangaClient.getFont().getWidth(s1) - 5), this.height - 20, new Color(-1));
        RenderingUtils.drawString(s2, (this.width - KangaClient.getFont().getWidth(s2) - 5), this.height - 30, new Color(-1));
        RenderingUtils.drawString(s3, (this.width - KangaClient.getFont().getWidth(s3) - 5), this.height - 40, new Color(-1));
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.popMatrix();
        this.lastX = mouseX;
        this.lastY = mouseY;
    }

    private void drawMinecraftButtons(int j) {
        this.buttonList
                .add(new KangaButton(1, 10, j, 75, 20, I18n.format("menu.singleplayer"), false));
        this.buttonList.add(
                new KangaButton(2, 10, j + 20, 75, 20, I18n.format("menu.multiplayer"), false));
        this.buttonList
                .add(new KangaButton(3, 10, j + 40, 75, 20, I18n.format("menu.options"), false));
        this.buttonList
                .add(new KangaButton(4, 10, j + 60, 75, 20, I18n.format("menu.quit"), false));
    }


    @Override
    public void actionPerformed(GuiButton button) {

        // Singleplayer
        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        }
        // Multiplayer
        if (button.id == 2) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }

        // MC Settings
        if (button.id == 3) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }

        // Exit
        if (button.id == 4) {
            this.mc.shutdown();
        }


        if (button.id == 6) {
            try {
                Desktop.getDesktop().browse(new URI("https://kangaClient.github.io"));
            } catch (URISyntaxException e) {
                KangaClient.getLogger().log(Level.ERROR, "Unable to open website + (" + e.getMessage() + ")");
            } catch (IOException e) {
                KangaClient.getLogger().log(Level.ERROR, "Unable to open website (" + e.getMessage() + ")");
            }
        }
    }

    private void renderBackground(int mouseX, int mouseY) {
        GL11.glPushMatrix();
        ScaledResolution sr = new ScaledResolution(this.mc);
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableAlpha();
        Minecraft.getMinecraft().getTextureManager().bindTexture(background);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(0.0D, sr.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
        worldrenderer.pos(sr.getScaledWidth(), sr.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
        worldrenderer.pos(sr.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
        worldrenderer.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();


        checkY(mouseX, mouseY);

        GlStateManager.translate(moveX, moveY, 0);
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, .0F, 1.0F);
        GL11.glPopMatrix();
    }

    private void checkY(int mouseX, int mouseY) {
        if (ValueRange.of(-10, 10).isValidIntValue((long) moveY)) {
            if (lastY > mouseY) {
                moveY -= 0.3f;
            }

            if (lastY < mouseY) {
                moveY += 0.3f;
            }


        }
        if (!ValueRange.of((long) -0.5, (long) 0.5).isValidIntValue((long) moveY)) {
            if (moveY < 0.0) {
                moveY += 0.2;
            }
            if (moveY > 0.0) {
                moveY -= 0.2;
            }
        }

        if (ValueRange.of(-10, 10).isValidIntValue((long) moveX)) {
            if (lastX > mouseX) {
                moveX -= 0.3f;
            }

            if (lastX < mouseX) {
                moveX += 0.3f;
            }


        }

        if (!ValueRange.of((long) -0.5, (long) 0.5).isValidIntValue((long) moveX)) {
            if (moveX < 0.0) {
                moveX += 0.2;
            }
            if (moveX > 0.0) {
                moveX -= 0.2;
            }
        }


    }

}