package com.example.spotifyplaylistcreator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.SpotifyApi;

import java.net.URI;


@Component
public class AuthUriCreator implements ApplicationRunner {

    @Autowired
    private SpotifyApi spotifyApi;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        URI authUri = spotifyApi.authorizationCodeUri()
                .scope("playlist-modify-private playlist-modify-public")
                .show_dialog(true).build().execute();
        System.out.println(authUri);
    }


}
