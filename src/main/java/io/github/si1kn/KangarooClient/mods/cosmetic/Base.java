package io.github.si1kn.KangarooClient.mods.cosmetic;

import net.minecraft.client.entity.AbstractClientPlayer;

public abstract class Base {
    public abstract void render(AbstractClientPlayer entityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
                                float netHeadYaw, float headPitch, float scale);
}
