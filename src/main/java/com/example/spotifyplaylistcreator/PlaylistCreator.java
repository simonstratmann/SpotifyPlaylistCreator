package com.example.spotifyplaylistcreator;


import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.special.SnapshotResult;
import se.michaelthelin.spotify.model_objects.specification.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;


@Component
public class PlaylistCreator {

    @Autowired
    private SpotifyApi spotifyApi;

    public void addToPlaylist(String code) throws IOException, SpotifyWebApiException, ParseException {
        AuthorizationCodeCredentials authorizationCodeCredentials = spotifyApi.authorizationCode(code).build().execute();
        spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
        addAlbums();
//        addASongs();
    }

    private void addASongs() throws IOException, SpotifyWebApiException, ParseException {
        final List<String> songNames = Arrays.asList(
                "L.O.E. - Secret Societies Rule the World",
                "Hubris. - The One Above",
                "Din of Celestial Birds - Downpour",
                "False Hope For The Savage - Little Empires",
                "Hubris. - Of Light",
                "L.O.E. - Lament",
                "Silent Whale Becomes A° Dream - North",
                "Dutch Elm - This Building is Intimidating",
                "A Troop of Echoes - The Great Divide",
                "November Might Be Fine - Hope",
                "Spurv - Til en ny vår",
                "Bowden - Abdication",
                "D. Majestic and the Spectral Band - The Tallest Man on the Moon",
                "Arctic Rise - A Species With Amnesia",
                "Passage - L'Abandon",
                "abriction - breaking through the clouds",
                "Shy, Low - Instinctual Estrangement",
                "A Troop of Echoes - Why But Also Yes",
                "Sarkh - Helios",
                "November Might Be Fine (feat. Methuselah) - TRUTH",
                "the abyss inside us - home"
        );

        final String playlistId = "4FYX9v8KjEWlr4Tl6jU6jf";



        for (String songname : songNames) {
            Paging<Track> page = spotifyApi.searchTracks(songname).build().execute();
            if (page.getItems().length > 0) {
                Track track = page.getItems()[0];
                String descriptor = Arrays.stream(track.getArtists()).map(ArtistSimplified::getName).collect(Collectors.joining(", ")) + " - " + track.getName();
                System.out.println("Found " + descriptor);
                spotifyApi.addItemsToPlaylist(playlistId, new String[]{track.getUri()}).build().execute();
                System.out.println("Added " + descriptor);
            }
        }
    }

    private void addAlbums() throws IOException, SpotifyWebApiException, ParseException {
        final List<String> albumNames = Arrays.asList(
                "VESUVE - PLINE",
                "Din of Celestial Birds - The Night is for Dreamers",
                "AV Sunset - Disconnections",
                "Timelapse Aurora - In Defeat, I See the Future",
                "Fury & Solace - For Our Sons",
                "Sound Collider - Anomalies",
                "ARKITECT[S] FAILURE - (RE)BIRTH",
                "Callused Wing - Dirge of the Nameless Explorer",
                "L.O.E - The World and Everything in It",
        "Kolyma - Exit Strategy"
        );

        //https://open.spotify.com/playlist/3kCBrUwKCGUE53jCams7gX?si=d869dac13a13444e
        final String playlistId = "3kCBrUwKCGUE53jCams7gX";


        Playlist playlist = spotifyApi.getPlaylist(playlistId).build().execute();
        Paging<PlaylistTrack> playlistTracks = playlist.getTracks();
        final List<String> playlistExistingTrackUris = new ArrayList<>(Arrays.stream(playlistTracks.getItems()).map(x -> x.getTrack().getUri()).toList());

        for (String albumName : albumNames) {
            Paging<AlbumSimplified> page = spotifyApi.searchAlbums(albumName).build().execute();
            if (page.getItems().length > 0) {
                AlbumSimplified item = page.getItems()[0];
                Album album = spotifyApi.getAlbum(item.getId()).build().execute();
                String[] trackUris = Arrays.stream(album.getTracks().getItems()).map(TrackSimplified::getUri).toList().toArray(new String[0]);
                String descriptor = Arrays.stream(item.getArtists()).map(ArtistSimplified::getName).collect(Collectors.joining(", ")) + " - " + item.getName();
                if (playlistExistingTrackUris.contains(trackUris[0])) {
                    System.out.println("Playlist already contains " + descriptor);
                    return;
                }
                System.out.println("Found " + descriptor);
                SnapshotResult addResult = spotifyApi.addItemsToPlaylist(playlistId, trackUris).build().execute();
                System.out.println("Added " + descriptor);
            }
        }
    }
}
