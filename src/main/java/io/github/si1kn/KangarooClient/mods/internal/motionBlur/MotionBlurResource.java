package io.github.si1kn.KangarooClient.mods.internal.motionBlur;

import java.io.InputStream;
import java.util.Locale;

import io.github.si1kn.KangarooClient.mods.internal.MotionBlurMod;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

public class MotionBlurResource implements IResource {
    private static final String JSON = "{\"targets\":[\"swap\",\"previous\"],\"passes\":[{\"name\":\"phosphor\",\"intarget\":\"minecraft:main\",\"outtarget\":\"swap\",\"auxtargets\":[{\"name\":\"PrevSampler\",\"id\":\"previous\"}],\"uniforms\":[{\"name\":\"Phosphor\",\"values\":[%.2f, %.2f, %.2f]}]},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"previous\"},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"minecraft:main\"}]}";

    public InputStream getInputStream() {
        double amount = 0.7D + MotionBlurMod.getBlurAmount() / 100.0D * 3.0D - 0.01D;
        return IOUtils.toInputStream(String.format(Locale.ENGLISH, "{\"targets\":[\"swap\",\"previous\"],\"passes\":[{\"name\":\"phosphor\",\"intarget\":\"minecraft:main\",\"outtarget\":\"swap\",\"auxtargets\":[{\"name\":\"PrevSampler\",\"id\":\"previous\"}],\"uniforms\":[{\"name\":\"Phosphor\",\"values\":[%.2f, %.2f, %.2f]}]},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"previous\"},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"minecraft:main\"}]}", amount, amount, amount));
    }

    public boolean hasMetadata() {
        return false;
    }

    public IMetadataSection getMetadata(String metadata) {
        return null;
    }

    public ResourceLocation getResourceLocation() {
        return null;
    }

    public String getResourcePackName() {
        return null;
    }
}
