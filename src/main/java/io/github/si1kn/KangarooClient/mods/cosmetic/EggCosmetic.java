package io.github.si1kn.KangarooClient.mods.cosmetic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class EggCosmetic extends Base {

    @Override
    public void render(AbstractClientPlayer entityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        GL11.glPushMatrix();
        float swing = ageInTicks * 15.0F;

        if (entityIn.isSneaking()) {
            GL11.glTranslatef(0.0F, 0.2625F, 0.0F);
        }
        GlStateManager.rotate(netHeadYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(headPitch, 1.0F, 0.0F, 0.0F);

        GL11.glRotatef(swing, 0.0F, 1.0F, 0.0F);
        GL11.glScalef(0.25F, -0.25F, 0.25F);
        GL11.glTranslatef(2.1F, 1.0F, 0.0F);
        ItemStack item = new ItemStack(Items.egg);
        Minecraft.getMinecraft().getItemRenderer().renderItem(entityIn, item, ItemCameraTransforms.TransformType.NONE);
        GL11.glPushMatrix();
        GL11.glTranslatef(-3.8F, 0.0F, 0.0F);
        Minecraft.getMinecraft().getItemRenderer().renderItem(entityIn, item, ItemCameraTransforms.TransformType.NONE);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef(-2.1F, 0.0F, 1.7F);
        Minecraft.getMinecraft().getItemRenderer().renderItem(entityIn, item, ItemCameraTransforms.TransformType.NONE);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef(-2.7F, 0.0F, -1.7F);
        Minecraft.getMinecraft().getItemRenderer().renderItem(entityIn, item, ItemCameraTransforms.TransformType.NONE);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }
}
