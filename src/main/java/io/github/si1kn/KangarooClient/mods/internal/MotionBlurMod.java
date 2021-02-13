package io.github.si1kn.KangarooClient.mods.internal;

import com.google.common.eventbus.Subscribe;
import io.github.si1kn.KangarooClient.events.ClientTickEvent;
import io.github.si1kn.KangarooClient.events.KeyPressEvent;
import io.github.si1kn.KangarooClient.mods.internal.motionBlur.MotionBlurResourceManager;
import io.github.si1kn.KangarooClient.utils.ReflectionUtils;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

@SuppressWarnings("ALL")
public class MotionBlurMod {

    @Getter
    @Setter
    private static double blurAmount;


    private final Minecraft mc = Minecraft.getMinecraft();


    @Getter
    @Setter
    private static boolean isEnabled;


    private Map domainResourceManagers;


    @Subscribe
    public void onClientTick(ClientTickEvent event) {
        if (this.domainResourceManagers == null)
            try {
                for (Field field : SimpleReloadableResourceManager.class.getDeclaredFields()) {
                    if (field.getType() == Map.class) {
                        field.setAccessible(true);
                        this.domainResourceManagers = (Map) field.get(Minecraft.getMinecraft().getResourceManager());


                        break;
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        if (!this.domainResourceManagers.containsKey("motionblur"))
            this.domainResourceManagers.put("motionblur", new MotionBlurResourceManager());

        if (!isEnabled) {
            domainResourceManagers.remove("motionblur");
        }
    }


    @Subscribe
    public void onKey(KeyPressEvent event) {
        if (this.mc.thePlayer != null && isEnabled)
            try {
                Method method = ReflectionUtils.findMethod(EntityRenderer.class, this.mc.entityRenderer, new String[]{"loadShader"}, ResourceLocation.class);
                method.invoke(this.mc.entityRenderer, new ResourceLocation("motionblur", "motionblur"));
                this.mc.entityRenderer.getShaderGroup().createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        if (!isEnabled()) {
            mc.entityRenderer.loadEntityShader(null);
        }
    }

}
