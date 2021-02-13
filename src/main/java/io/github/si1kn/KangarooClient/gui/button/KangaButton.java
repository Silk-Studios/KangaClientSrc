package io.github.si1kn.KangarooClient.gui.button;


import io.github.si1kn.KangarooClient.utils.RenderingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

public class KangaButton extends GuiButton {

    public final boolean drawOutline;
    Color color = new Color(0, 255, 255);

    public KangaButton(int buttonID, int xPos, int yPos, int width, int height, String text, boolean drawOutline) {
        super(buttonID, xPos, yPos, width, height, text);
        this.drawOutline = drawOutline;
    }


    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            FontRenderer fontrenderer = mc.fontRendererObj;
            // mc.getTextureManager().bindTexture(buttonTextures);

            GlStateManager.color(color.getRed(), color.getGreen(), color.getBlue(), 1.0F);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width
                    && mouseY < this.yPosition + this.height;
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            this.mouseDragged(mc, mouseX, mouseY);
            int j = 14737632;

            if (!this.enabled) {
                j = 10526880;
            } else if (this.hovered) {
                j = 0xffffff;
            }

            if (drawOutline) {
                RenderingUtils.drawOutlinedRect(this.xPosition, this.yPosition, this.xPosition + this.width,
                        this.yPosition + this.height, 805306368, Color.OPAQUE);
            }

            if (this.enabled && this.hovered) {
                drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height,
                        805306368);
                this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2,
                        this.yPosition + (this.height - 8) / 2, -1);
            }

            this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2,
                    this.yPosition + (this.height - 8) / 2, j);
        }
    }
}