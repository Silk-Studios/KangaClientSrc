package io.github.si1kn.KangarooClient.utils;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadDownloadImageData2 extends SimpleTexture {
    private static final Logger logger = LogManager.getLogger();
    private static final AtomicInteger threadDownloadCounter = new AtomicInteger(0);
    private final File cacheFile;
    private final String imageUrl;
    private final IImageBuffer imageBuffer;
    private BufferedImage bufferedImage;
    private Thread imageThread;
    private boolean textureUploaded;
    public Boolean imageFound = null;
    public boolean pipeline = false;

    public ThreadDownloadImageData2(File cacheFileIn, String imageUrlIn, ResourceLocation textureResourceLocation, IImageBuffer imageBufferIn) {
        super(textureResourceLocation);
        this.cacheFile = cacheFileIn;
        this.imageUrl = imageUrlIn;
        this.imageBuffer = imageBufferIn;
    }

    private void checkTextureUploaded() {
        if (!this.textureUploaded) {
            if (this.bufferedImage != null) {
                if (this.textureLocation != null) {
                    this.deleteGlTexture();
                }

                TextureUtil.uploadTextureImage(super.getGlTextureId(), this.bufferedImage);
                this.textureUploaded = true;
            }
        }
    }

    public int getGlTextureId() {
        this.checkTextureUploaded();
        return super.getGlTextureId();
    }

    public void setBufferedImage(BufferedImage bufferedImageIn) {
        this.bufferedImage = bufferedImageIn;

        if (this.imageBuffer != null) {
            this.imageBuffer.skinAvailable();
        }

        this.imageFound = Boolean.valueOf(this.bufferedImage != null);
    }

    public void loadTexture(IResourceManager resourceManager) throws IOException {
        if (this.bufferedImage == null && this.textureLocation != null) {
            super.loadTexture(resourceManager);
        }

        if (this.imageThread == null) {
            if (this.cacheFile != null && this.cacheFile.isFile()) {
                logger.debug("Loading http texture from local cache ({})", this.cacheFile);

                try {
                    this.bufferedImage = ImageIO.read(this.cacheFile);

                    if (this.imageBuffer != null) {
                        this.setBufferedImage(this.imageBuffer.parseUserSkin(this.bufferedImage));
                    }
                } catch (IOException ioexception) {
                    logger.error("Couldn't load skin " + this.cacheFile, ioexception);
                    this.loadTextureFromServer();
                }
            } else {
                this.loadTextureFromServer();
            }
        }
    }

    protected void loadTextureFromServer() {
        this.imageThread = new Thread("Texture Downloader #" + threadDownloadCounter.incrementAndGet()) {
            public void run() {
                HttpURLConnection httpurlconnection = null;
                ThreadDownloadImageData2.logger.debug("Downloading http texture from {} to {}",
                        ThreadDownloadImageData2.this.imageUrl, ThreadDownloadImageData2.this.cacheFile);

                try {
                    httpurlconnection = (HttpURLConnection) (new URL(ThreadDownloadImageData2.this.imageUrl))
                            .openConnection(Minecraft.getMinecraft().getProxy());
                    httpurlconnection.setDoInput(true);
                    httpurlconnection.setDoOutput(false);
                    httpurlconnection.connect();

                    if (httpurlconnection.getResponseCode() / 100 == 2) {
                        BufferedImage bufferedimage;

                        if (ThreadDownloadImageData2.this.cacheFile != null) {
                            FileUtils.copyInputStreamToFile(httpurlconnection.getInputStream(),
                                    ThreadDownloadImageData2.this.cacheFile);
                            bufferedimage = ImageIO.read(ThreadDownloadImageData2.this.cacheFile);
                        } else {
                            bufferedimage = TextureUtil.readBufferedImage(httpurlconnection.getInputStream());
                        }

                        if (ThreadDownloadImageData2.this.imageBuffer != null) {
                            bufferedimage = ThreadDownloadImageData2.this.imageBuffer.parseUserSkin(bufferedimage);
                        }

                        ThreadDownloadImageData2.this.setBufferedImage(bufferedimage);
                        return;
                    }
                } catch (Exception exception) {
                    ThreadDownloadImageData2.logger.error("Couldn't download http texture",
                            exception);
                    return;
                } finally {
                    if (httpurlconnection != null) {
                        httpurlconnection.disconnect();
                    }
                }
            }
        };
        this.imageThread.setDaemon(true);
        this.imageThread.start();
    }
}