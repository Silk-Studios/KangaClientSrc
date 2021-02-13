package io.github.si1kn.KangarooClient.mixins.client.renderer;

import io.github.si1kn.KangarooClient.mods.internal.OldAnimations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Final
    @Shadow
    Minecraft mc;
    @Shadow
    private
    ItemStack itemToRender;
    @Shadow
    private float equippedProgress;
    @Shadow
    private float prevEquippedProgress;

    @Shadow
    abstract void func_178101_a(float angle, float p_178101_2_);

    @Shadow
    abstract void func_178109_a(AbstractClientPlayer clientPlayer);

    @Shadow
    abstract void func_178110_a(EntityPlayerSP entityplayerspIn, float partialTicks);

    @Shadow
    abstract void renderItemMap(AbstractClientPlayer clientPlayer, float p_178097_2_, float p_178097_3_, float p_178097_4_);

    @Shadow
    abstract void transformFirstPersonItem(float equipProgress, float swingProgress);

    @Shadow
    abstract void func_178104_a(AbstractClientPlayer clientPlayer, float p_178104_2_);

    @Shadow
    abstract void func_178103_d();

    @Shadow
    abstract void func_178098_a(float p_178098_1_, AbstractClientPlayer clientPlayer);

    @Shadow
    abstract void func_178105_d(float p_178105_1_);

    @Shadow
    abstract void renderItem(EntityLivingBase entityIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform);

    @Shadow
    abstract void func_178095_a(AbstractClientPlayer clientPlayer, float p_178095_2_, float p_178095_3_);

    /**
     * @author Si1kn
     */
    @Overwrite
    public void renderItemInFirstPerson(float partialTicks) {
        float f = 1.0F - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks);
        EntityPlayerSP player = this.mc.thePlayer;
        float f1 = player.getSwingProgress(partialTicks);
        float f2 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;
        float f3 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * partialTicks;
        this.func_178101_a(f2, f3);
        this.func_178109_a(player);
        this.func_178110_a(player, partialTicks);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();

        if (this.itemToRender != null) {
            if (this.itemToRender.getItem() instanceof ItemMap) {
                this.renderItemMap(player, f2, f, f1);
            } else if (player.getItemInUseCount() > 0) {
                EnumAction enumaction = this.itemToRender.getItemUseAction();

                switch (enumaction) {
                    case NONE:
                        this.transformFirstPersonItem(f, 0.0F);
                        break;

                    case EAT:
                    case DRINK:
                        if (OldAnimations.isDrink()) {
                            this.func_178104_a(player, partialTicks);
                            this.transformFirstPersonItem(f, f1);
                        } else {
                            this.func_178104_a(player, partialTicks);
                            this.transformFirstPersonItem(f, 0.0F);
                        }
                        break;

                    case BLOCK:
                        if (OldAnimations.isBlock()) {
                            this.transformFirstPersonItem(0.2F, f1);
                            this.func_178103_d();
                            GlStateManager.translate(-0.5F, 0.2F, 0.0F);
                        } else {
                            this.transformFirstPersonItem(f, 0.0F);
                            this.func_178103_d();
                        }
                        break;
                    case BOW:
                        if (OldAnimations.isBow()) {
                            this.transformFirstPersonItem(f, f1);

                        } else {
                            this.transformFirstPersonItem(f, 0.0F);
                        }
                        this.func_178098_a(partialTicks, player);

                }
            } else {
                this.func_178105_d(f1);
                this.transformFirstPersonItem(f, f1);
            }

            this.renderItem(player, this.itemToRender, ItemCameraTransforms.TransformType.FIRST_PERSON);
        } else if (!player.isInvisible()) {
            this.func_178095_a(player, f, f1);
        }

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }
}
