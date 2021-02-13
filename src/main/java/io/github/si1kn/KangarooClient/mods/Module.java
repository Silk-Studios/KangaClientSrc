package io.github.si1kn.KangarooClient.mods;

import io.github.si1kn.KangarooClient.gui.hud.Position;

public abstract class Module {
    public Position pos = new Position(0, 0);

    public abstract void render(Position pos);

    public abstract double getWidth();

    public abstract double getHeight();


    public void load() {

    }


}
