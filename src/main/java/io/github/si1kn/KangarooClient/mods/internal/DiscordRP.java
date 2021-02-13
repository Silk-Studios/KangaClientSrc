package io.github.si1kn.KangarooClient.mods.internal;


import io.github.si1kn.KangarooClient.KangaClient;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.minecraft.client.Minecraft;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DiscordRP implements Runnable {


    private final String applicationId = "805254477795557386";


    public void start() {
        DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler((user) -> {}).build();


        DiscordRPC.discordInitialize(applicationId, handlers, true);


        ScheduledExecutorService executor = Executors
                .newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this, 0, 5, TimeUnit.SECONDS);


        createNewPresence("Loading client...", "");
    }

    public void stop() {
        DiscordRPC.discordShutdown();
    }

    @Override
    public void run() {
        DiscordRPC.discordRunCallbacks();
    }

    public static void createNewPresence(String line1, String line2) {
        DiscordRichPresence rich = new DiscordRichPresence.Builder(line1).setDetails(line2).setBigImage("logo", "Kanga Client").build();
        DiscordRPC.discordUpdatePresence(rich);
    }

    public static void playingOnServer(String ip) {
        if (ip.toLowerCase().contains("hypixel")) {
            DiscordRichPresence rich = new DiscordRichPresence.Builder("Playing on server: " + ip).setDetails("Playing: 1.8.9 / " + KangaClient.getInstance().getVersion()).setSmallImage("hypixel", "hypixel.net").setBigImage("logo", "Playing as: " + Minecraft.getMinecraft().getSession().getUsername()).build();
            DiscordRPC.discordUpdatePresence(rich);
        } else {
            DiscordRP.createNewPresence("Playing on a server: " + ip, "Playing: 1.8.9 / " + KangaClient.getInstance().getVersion());
        }
    }
}
