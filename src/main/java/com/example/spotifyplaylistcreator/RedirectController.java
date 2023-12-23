package com.example.spotifyplaylistcreator;

import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;

@RestController
public class RedirectController {

    @Autowired
    private PlaylistCreator playlistCreator;

    @GetMapping("/redirect")
    public void redirectGet(@RequestParam String code) throws IOException, ParseException, SpotifyWebApiException {
        playlistCreator.addToPlaylist(code);
    }

}
