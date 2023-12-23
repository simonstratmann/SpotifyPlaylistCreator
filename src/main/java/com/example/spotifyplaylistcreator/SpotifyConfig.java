package com.example.spotifyplaylistcreator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.michaelthelin.spotify.SpotifyApi;

import java.net.URI;

@Configuration
public class SpotifyConfig {

    @Value("${spotifyClientId}")
    private String spotifyClientId;
    @Value("${spotifyClientSecret}")
    private String spotifyClientSecret;

    @Bean
    public SpotifyApi spotifyApi() {
        return new SpotifyApi.Builder()
                .setClientId(spotifyClientId)
                .setClientSecret(spotifyClientSecret)
                .setRedirectUri(URI.create("http://127.0.0.1:8000/redirect"))
                .build();
    }
}
