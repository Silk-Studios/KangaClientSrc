package io.github.si1kn.KangarooClient.keybinds;

import net.minecraft.client.settings.KeyBinding;

public abstract class Keybind extends KeyBinding {

    private int key;
    private boolean pressed;

    public Keybind(String description, int keyCode) {
        super(description, keyCode, "Kanga keybinds");
        this.key = keyCode;
    }

    public void setWasPressed(boolean wasPressed) {
        this.pressed = wasPressed;
    }

    @Override
    public int getKeyCode() {
        return this.key;
    }

    @Override
    public void setKeyCode(int keyC) {
        this.key = keyC;
        super.setKeyCode(key);
    }


    public abstract void onPress();

    public abstract void onRelease();

    public abstract String getDescription();

}
