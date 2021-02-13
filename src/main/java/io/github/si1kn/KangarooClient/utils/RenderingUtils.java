package io.github.si1kn.KangarooClient.utils;

import io.github.si1kn.KangarooClient.KangaClient;
import io.github.si1kn.KangarooClient.gui.MainMenuGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class RenderingUtils {

    public static void drawHorizontalLine(int p_drawHorizontalLine_1_, int p_drawHorizontalLine_2_, int p_drawHorizontalLine_3_, int p_drawHorizontalLine_4_) {
        if (p_drawHorizontalLine_2_ < p_drawHorizontalLine_1_) {
            int lvt_5_1_ = p_drawHorizontalLine_1_;
            p_drawHorizontalLine_1_ = p_drawHorizontalLine_2_;
            p_drawHorizontalLine_2_ = lvt_5_1_;
        }

        Gui.drawRect(p_drawHorizontalLine_1_, p_drawHorizontalLine_3_, p_drawHorizontalLine_2_ + 1, p_drawHorizontalLine_3_ + 1, p_drawHorizontalLine_4_);
    }


    public static void drawOutlinedRect(int left, int top, int right, int bottom, int rectColor, int outlineColor) {
        Gui.drawRect(left + 1, top, right - 1, bottom, rectColor);

        drawHorizontalLine(left, right - 1, top, outlineColor);
        drawHorizontalLine(left, right - 1, bottom, outlineColor);
        drawVerticalLine(left, top, bottom, outlineColor);
        drawVerticalLine(right - 1, top, bottom, outlineColor);
    }

    public static void drawVerticalLine(int p_drawVerticalLine_1_, int p_drawVerticalLine_2_, int p_drawVerticalLine_3_, int p_drawVerticalLine_4_) {
        if (p_drawVerticalLine_3_ < p_drawVerticalLine_2_) {
            int lvt_5_1_ = p_drawVerticalLine_2_;
            p_drawVerticalLine_2_ = p_drawVerticalLine_3_;
            p_drawVerticalLine_3_ = lvt_5_1_;
        }

        Gui.drawRect(p_drawVerticalLine_1_, p_drawVerticalLine_2_ + 1, p_drawVerticalLine_1_ + 1, p_drawVerticalLine_3_, p_drawVerticalLine_4_);
    }


    public static void drawBlur() {
        if (!(Minecraft.getMinecraft().currentScreen instanceof MainMenuGui)) {
            ReflectionUtils.genericInvokeMethod(Minecraft.getMinecraft().entityRenderer, "loadShader", new ResourceLocation("shaders/post/blur.json"));
        }
    }


    public static void drawString(String text, float x, float y, Color color) {
        KangaClient.getFont().drawString(text, x, y, color.getRGB());
    }


    public static void drawHollowRect(int x, int y, int w, int h, int color) {
        drawHorizontalLine(x, x + w - 0.8f, y, color);
        drawHorizontalLine(x, x + w - 0.8f, y + h, color);
        drawVerticalLine(x, y + h, y - 0.8f, color);
        drawVerticalLine(x + w, y + h, y - 0.8f, color);
    }

    public static void drawHorizontalLine(float startX, float endX, float y, int color) {
        if (endX < startX) {
            int i = (int) startX;
            startX = endX;
            endX = i;
        }

        drawRect(startX, y, endX + 1, y + 0.4f, color);
    }

    public static void drawVerticalLine(float x, float startY, float endY, int color) {
        if (endY < startY) {
            int i = (int) startY;
            startY = endY;
            endY = i;
        }
        drawRect(x, startY + 1, x + 0.5f, endY, color);
    }


    public static void drawRect(double left, double top, double right, double bottom, int color) {
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, top, 0.0D).endVertex();
        worldrenderer.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();

    }

}
