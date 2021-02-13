package io.github.si1kn.KangarooClient.keybinds.impl;


import io.github.si1kn.KangarooClient.gui.rShift.RShiftGui;
import io.github.si1kn.KangarooClient.keybinds.Keybind;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class RShiftKeybind extends Keybind {

    public RShiftKeybind() {
        super("Opens the settings gui!", Keyboard.KEY_RSHIFT);
    }

    @Override
    public void onPress() {
        Minecraft.getMinecraft().displayGuiScreen(new RShiftGui());
    }

    @Override
    public void onRelease() {
    }

    @Override
    public String getDescription() {
        return "Opens the settings gui!";
    }
}
