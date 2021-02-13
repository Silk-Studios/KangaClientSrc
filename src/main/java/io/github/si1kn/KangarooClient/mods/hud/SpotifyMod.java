package io.github.si1kn.KangarooClient.mods.hud;

import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlayingContext;
import io.github.si1kn.KangarooClient.gui.hud.Position;
import io.github.si1kn.KangarooClient.mods.Module;
import io.github.si1kn.KangarooClient.mods.hud.spotify.GetSpotify;
import io.github.si1kn.KangarooClient.utils.ThreadDownloadImageData2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FilenameUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;


public class SpotifyMod extends Module {

    public static boolean keepGoing = true;
    private GetSpotify spotify;
    private String songLength = "0:00";
    private String songProgress = "0:00";
    private String songName = "No song currently playing";
    private String songArtists = "";
    private Integer songLengthAsInt = 0;
    private Integer songFullMs = 0;
    private ResourceLocation resourceLocation = new ResourceLocation("kangaClient/mods/spotify/unknown.jpg");


    private GetSpotify.SpotifyTrackCallback spotifyTrack;

    @Override
    public void render(Position pos) {
        Gui.drawRect((int) pos.getX(), (int) pos.getY(), (int) ((int) pos.getX() + getWidth()), (int) ((int) pos.getY() + getHeight()), new Color(49, 49, 49, 196).getRGB());
        Minecraft.getMinecraft().fontRendererObj.drawString("Song name: " + this.songName, (int) pos.getX() + 3, (int) pos.getY() + 8, -1);
        Minecraft.getMinecraft().fontRendererObj.drawString("Author: " + this.songArtists, (int) pos.getX() + 3, (int) pos.getY() + 18, -1);
        Minecraft.getMinecraft().fontRendererObj.drawString(this.songLength, (int) ((int) pos.getX() + getWidth() - 20), (int) pos.getY() + 38, -1);
        Minecraft.getMinecraft().fontRendererObj.drawString(this.songProgress, (int) pos.getX() + 3, (int) pos.getY() + 38, -1);

        int width = (int) ((getWidth() / TimeUnit.MILLISECONDS.toSeconds(this.songFullMs)) * TimeUnit.MILLISECONDS.toSeconds(this.songLengthAsInt));
        Gui.drawRect((int) pos.getX(), (int) pos.getY() + 50, (int) pos.getX() + width, (int) pos.getY() + 47, -1);

        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);


        Gui.drawScaledCustomSizeModalRect((int) pos.getX() - 50, (int) pos.getY(), 0.0F, 0.0F, 1000, 1000, 50, (int) getHeight(), 1000, 1000);
    }

    @Override
    public double getWidth() {
        if (Minecraft.getMinecraft().fontRendererObj.getStringWidth(this.songName) < Minecraft.getMinecraft().fontRendererObj.getStringWidth(this.songArtists)) {
            return 70 + Minecraft.getMinecraft().fontRendererObj.getStringWidth(this.songArtists);
        } else {
            return 70 + Minecraft.getMinecraft().fontRendererObj.getStringWidth(this.songName);
        }
    }

    @Override
    public double getHeight() {
        return 50;
    }

    public void stop() {
        keepGoing = false;
    }


    private void setCurrentSongDetails(CurrentlyPlayingContext currentlyPlayingContext) {

        this.songLength = "" + getCurrentProgress(currentlyPlayingContext, false) / 60 + ":" + getCurrentProgress(currentlyPlayingContext, false) % 60;
        this.songLengthAsInt = currentlyPlayingContext.getProgress_ms();
        this.songName = currentlyPlayingContext.getItem().getName();
        this.songProgress = getCurrentProgress(currentlyPlayingContext, true) / 60 + ":" + getCurrentProgress(currentlyPlayingContext, true) % 60;
        this.songFullMs = currentlyPlayingContext.getItem().getDurationMs();
        this.downloadAndSetTexture(currentlyPlayingContext.getItem().getAlbum().getImages()[0].getUrl(), rl -> resourceLocation = rl);

        //   System.out.print(currentlyPlayingContext.getItem().getAlbum().getImages()[0].getUrl());
        if (Arrays.stream(currentlyPlayingContext.getItem().getArtists()).findFirst().isPresent()) {
            this.songArtists = Arrays.stream(currentlyPlayingContext.getItem().getArtists()).findFirst().get().getName();
        } else {
            this.songArtists = "Error! Please report this!";
        }

    }


    private void refreshNonPlayingSongDataUI() {
        this.songName = "No song currently playing";
        this.songArtists = "";
        this.songProgress = "0:00";
        this.songLength = "0:00";
        this.songFullMs = 0;
        this.songLengthAsInt = 0;
        this.resourceLocation = new ResourceLocation("kangaClient/mods/spotify/unknown.jpg");
    }

    private int getCurrentProgress(CurrentlyPlayingContext currentlyPlayingContext, boolean b) {
        if (b) {
            return currentlyPlayingContext.getProgress_ms() / 1000;
        } else {
            return currentlyPlayingContext.getItem().getDurationMs() / 1000;
        }
    }


    private void refreshSongDataUI(CurrentlyPlayingContext currentlyPlayingContext) {
        setCurrentSongDetails(currentlyPlayingContext);
        spotify.getCountdownLatch().countDown();
    }


    @Override
    public void load() {
        keepGoing = true;
        Thread spotifyThread = new Thread(() -> {
            spotify = new GetSpotify(
                    Constants.CLIENT_ID,
                    Constants.CLIENT_SECRET,
                    50400
            );

            try {
                spotify.authorize(
                        SpotifyPermissions.USER_READ_CURRENTLY_PLAYING,
                        SpotifyPermissions.USER_READ_PLAYBACK_STATE,
                        SpotifyPermissions.USER_MODIFY_PLAYBACK_STATE
                );
            } catch (Exception exception) {
                exception.printStackTrace();
            }


            spotify.initCallbackThread(this.spotifyTrack = new GetSpotify.SpotifyTrackCallback() {


                @Override
                public void setCurrentLength(CurrentlyPlayingContext currentlyPlayingContext) {
                    setCurrentSongDetails(currentlyPlayingContext);
                }

                @Override
                public void refreshSongData(CurrentlyPlayingContext currentlyPlayingContext) {
                    refreshSongDataUI(currentlyPlayingContext);

                }

                @Override
                public void refreshNonPlayingSongData() {
                    refreshNonPlayingSongDataUI();
                }
            });
        });
        spotifyThread.start();
    }

    @SuppressWarnings("ALL")
    private static class Constants {
        public static final String CLIENT_ID = "b5cd36246f5046e8ba6a251ddde91f3c";
        public static final String CLIENT_SECRET = "238bfefe58b546e2a1cbb5e0fbfef62a";
    }

    private static class SpotifyPermissions {
        public static final String USER_READ_PLAYBACK_STATE = "user-read-playback-state";
        public static final String USER_READ_CURRENTLY_PLAYING = "user-read-currently-playing";
        public static final String USER_MODIFY_PLAYBACK_STATE = "user-modify-playback-state";
    }


    private void downloadAndSetTexture(String url, ResourceLocationCallback callback) {

        if (url != null && !url.isEmpty()) {
            String baseName = FilenameUtils.getBaseName(url);
            final ResourceLocation resourcelocation = new ResourceLocation("pain_temp/" + baseName);
            TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
            ITextureObject itextureobject = texturemanager.getTexture(resourcelocation);

            if (itextureobject != null && itextureobject instanceof ThreadDownloadImageData2) {
                ThreadDownloadImageData2 threaddownloadimagedata = (ThreadDownloadImageData2) itextureobject;

                if (threaddownloadimagedata.imageFound != null) {
                    if (threaddownloadimagedata.imageFound.booleanValue()) {
                        callback.onTextureLoaded(resourcelocation);
                    }
                    return;
                }
            }

            IImageBuffer iimagebuffer = new IImageBuffer() {
                public BufferedImage parseUserSkin(BufferedImage image) {
                    return image;
                }

                public void skinAvailable() {
                    callback.onTextureLoaded(resourcelocation);
                }
            };
            ThreadDownloadImageData2 threaddownloadimagedata1 = new ThreadDownloadImageData2(null, url, null, iimagebuffer);
            threaddownloadimagedata1.pipeline = true;
            texturemanager.loadTexture(resourcelocation, threaddownloadimagedata1);
        }
    }

    public interface ResourceLocationCallback {
        void onTextureLoaded(ResourceLocation rl);
    }


}
