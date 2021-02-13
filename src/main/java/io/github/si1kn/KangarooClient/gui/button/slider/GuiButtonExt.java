package io.github.si1kn.KangarooClient.gui.button.slider;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;


public class GuiButtonExt extends GuiButton {
    public GuiButtonExt(int id, int xPos, int yPos, String displayString) {
        super(id, xPos, yPos, displayString);
    }

    public GuiButtonExt(int id, int xPos, int yPos, int width, int height, String displayString) {
        super(id, xPos, yPos, width, height, displayString);
    }

    /**
     * Draws this button to the Screen.
     */
    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (visible) {
            hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
            int k = getHoverState(hovered);
            GuiUtils.drawContinuousTexturedBox(buttonTextures, xPosition, yPosition, 0, 46 + k * 20, width, height,
                    200, 20, 2, 3, 2, 2, zLevel);
            mouseDragged(mc, mouseX, mouseY);
            int color = 14737632;

            if (!enabled) {
                color = 10526880;
            } else if (hovered) {
                color = 16777120;
            }

            String buttonText = displayString;
            int strWidth = mc.fontRendererObj.getStringWidth(buttonText);
            int ellipsisWidth = mc.fontRendererObj.getStringWidth("...");

            if (strWidth > width - 6 && strWidth > ellipsisWidth) {
                buttonText = mc.fontRendererObj.trimStringToWidth(buttonText, width - 6 - ellipsisWidth).trim() + "...";
            }

            drawCenteredString(mc.fontRendererObj, buttonText, xPosition + width / 2, yPosition + (height - 8) / 2, color);
        }
    }
}
