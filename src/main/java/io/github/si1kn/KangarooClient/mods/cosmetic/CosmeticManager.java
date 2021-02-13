package io.github.si1kn.KangarooClient.mods.cosmetic;

import com.google.gson.Gson;
import lombok.Getter;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class CosmeticManager implements LayerRenderer<AbstractClientPlayer> {

    private final Gson gson = new Gson();

    @Getter
    private static Map<AbstractClientPlayer, String> listOfBetaCapes = new HashMap<>();

    private final EggCosmetic eggCosmetic;
    private final KangaCape kangaCape;

    private final String backupString = "{\"cosmetic\":{\"egg\":false,\"betaCape\":false,\"ownerCape\":false},\"doesHaveBeta\":false}";


    public CosmeticManager(ModelPlayer mainModel) {
        this.eggCosmetic = new EggCosmetic();
        this.kangaCape = new KangaCape(mainModel);
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer entityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        Decompile decompile;
        if (listOfBetaCapes.containsKey(entityIn)) {
            decompile = this.gson.fromJson(listOfBetaCapes.get(entityIn), Decompile.class);
        } else {
            decompile = doesHaveCosmetic(entityIn);
        }

        if (decompile.cosmetic.egg && !entityIn.isInvisible()) {
            this.eggCosmetic.render(entityIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
        }
        if (decompile.cosmetic.betaCape && !entityIn.isInvisible()) {
            kangaCape.render(entityIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
        }

    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }


    public Decompile doesHaveCosmetic(AbstractClientPlayer player) {
        if (listOfBetaCapes.containsKey(player)) {
            return gson.fromJson(listOfBetaCapes.get(player), Decompile.class);
        } else {
            Multithreading.runAsync(() -> {
                String baseUuid = player.getUniqueID().toString();
                String usableUUID = baseUuid.replace("-", "");
                try {
                    if (!listOfBetaCapes.containsKey(player))
                        listOfBetaCapes.put(player, new Scanner(new URL("https://raw.githubusercontent.com/Silk-Studios/KangaCosmetics/main/" + usableUUID + ".json").openStream(), "UTF-8").useDelimiter("A").next());
                } catch (IOException exception) {
                    listOfBetaCapes.put(player, backupString);
                    System.err.println("Could not download " + player.getName() + "'s cosmetics! Defaulting to backup.");
                }
            });

        }
        return null;
    }


    public static class Decompile {

        public CosmeticGson cosmetic;
        public boolean doesHaveBeta;
    }

    public static class CosmeticGson {
        public boolean egg;
        public boolean betaCape;
        public boolean ownerCape;
    }


    private static class Multithreading {
        private static final AtomicInteger threadCounter = new AtomicInteger(0);
        private static final ExecutorService SERVICE = Executors.newCachedThreadPool(task -> new Thread(task, "Cosmetic" + threadCounter.getAndIncrement()));

        public static void runAsync(Runnable task) {
            SERVICE.execute(task);
        }
    }

}
