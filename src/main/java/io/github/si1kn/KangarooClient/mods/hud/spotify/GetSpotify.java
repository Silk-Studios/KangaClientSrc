package io.github.si1kn.KangarooClient.mods.hud.spotify;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlayingContext;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.data.player.GetInformationAboutUsersCurrentPlaybackRequest;
import io.github.si1kn.KangarooClient.mods.hud.SpotifyMod;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


@SuppressWarnings("ALL")
public class GetSpotify {


    private final SpotifyApi spotifyApi;
    private AuthorizationCodeRequest codeRequest;
    private String code;
    private GetInformationAboutUsersCurrentPlaybackRequest playbackStatus;
    private AuthorizationCodeRefreshRequest authorizationCodeRefreshRequest;
    private AuthorizationCodeCredentials authorizationCodeCredentials;
    private final CountDownLatch countdownLatch = new CountDownLatch(1);
    private final AtomicBoolean updatingAccess = new AtomicBoolean(false);

    private Track previousSong;
    private final int portNum;

    public GetSpotify(String clientId, String clientSecret, int portNum) {
        spotifyApi = new SpotifyApi.Builder().setClientId(clientId).setClientSecret(clientSecret).setRedirectUri(SpotifyHttpManager.makeUri("http://localhost:" + portNum)).build();
        this.portNum = portNum;
    }

    public void authorize(String... permissions) throws Exception {
        final AuthorizationCodeUriRequest uriRequest = spotifyApi.authorizationCodeUri().scope(String.join(",", permissions)).build();
        runServer(uriRequest);
        codeRequest = spotifyApi.authorizationCode(code).build();
        authorizationCode_Sync(codeRequest);
        startRefreshThread();
        playbackStatus = spotifyApi.getInformationAboutUsersCurrentPlayback().build();
    }

    public CountDownLatch getCountdownLatch() {
        return countdownLatch;
    }

    public SpotifyApi getSpotifyApi() {
        return spotifyApi;
    }

    private void openLinkInBrowser(AuthorizationCodeUriRequest uriRequest) {
        URI finalUri = uriRequest.execute();
        try {
            Desktop.getDesktop().browse(finalUri);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void authorizationCode_Sync(AuthorizationCodeRequest codeRequest) {
        try {
            final AuthorizationCodeCredentials authorizationCodeCredentialsTemp = codeRequest.execute();

            spotifyApi.setAccessToken(authorizationCodeCredentialsTemp.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentialsTemp.getRefreshToken());
            authorizationCodeCredentials = authorizationCodeCredentialsTemp;
            authorizationCodeRefreshRequest = spotifyApi.authorizationCodeRefresh().build();
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private void runServer(AuthorizationCodeUriRequest uriRequest) throws Exception {
        final ExecutorService ex = Executors.newSingleThreadExecutor();

        HttpServer server = HttpServer.create(new InetSocketAddress(portNum), 0);

        server.createContext("/", (HttpExchange h) -> {
            code = h.getRequestURI().getQuery().substring(5);
            h.sendResponseHeaders(200, 0);
            OutputStream responseBody = h.getResponseBody();
            responseBody.write("<html><body onload=\"self.close(); self.close();\">Success! Your spotify account has been linked! You may close this window.</body></html>".getBytes());
            responseBody.close();
            countdownLatch.countDown();
        });

        server.setExecutor(ex);
        server.start();
        openLinkInBrowser(uriRequest);
        countdownLatch.await();
        ex.shutdown();
        ex.awaitTermination(1, TimeUnit.HOURS);
        server.stop(0);
    }

    private void startRefreshThread() {
        Thread updateCredentials = new Thread() {

            @Override
            public void run() {
                while (true) {
                    try {
                        AuthorizationCodeCredentials temp = authorizationCodeCredentials;
                        sleep(temp.getExpiresIn() * 1000);
                        updatingAccess.set(true);
                        temp = authorizationCodeRefreshRequest.execute();
                        spotifyApi.setAccessToken(temp.getAccessToken());
                        spotifyApi.setRefreshToken(temp.getRefreshToken());
                        updatingAccess.set(false);
                    } catch (InterruptedException | SpotifyWebApiException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        updateCredentials.setDaemon(true);
        updateCredentials.setName("Erics Spotify OAuth Thingy - Refresh Credentials");
        updateCredentials.start();
    }

    public void initCallbackThread(SpotifyTrackCallback callback) {
        Thread refreshData = new Thread() {

            @Override
            public void run() {
                CurrentlyPlayingContext currentlyPlayingContext = null;
                Track currentSong;
                while (SpotifyMod.keepGoing) {
                    try {
                        if (!updatingAccess.get()) {
                            currentlyPlayingContext = playbackStatus.execute();
                            if (currentlyPlayingContext.getIs_playing()) {
                                currentSong = currentlyPlayingContext.getItem();
                                if (currentSong == null) {
                                    callback.refreshNonPlayingSongData();
                                } else {
                                    if (currentSong.equals(previousSong)) {
                                        callback.setCurrentLength(currentlyPlayingContext);
                                    } else {
                                        previousSong = currentSong;
                                        callback.refreshSongData(currentlyPlayingContext);
                                    }
                                }
                            } else {
                                callback.refreshNonPlayingSongData();
                            }
                        }
                    } catch (SpotifyWebApiException | IOException e1) {
                        e1.printStackTrace();
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        };
        refreshData.setDaemon(true);
        refreshData.setName("Erics Spotify OAuth Thingy - Refresh Data");
        refreshData.start();
    }


    public interface SpotifyTrackCallback {

        void refreshNonPlayingSongData();

        void setCurrentLength(CurrentlyPlayingContext currentlyPlayingContext);

        void refreshSongData(CurrentlyPlayingContext currentlyPlayingContext) throws SpotifyWebApiException, IOException;

    }
}
