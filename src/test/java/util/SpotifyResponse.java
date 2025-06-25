package util;

import org.json.JSONArray;
import org.json.JSONObject;
import pojo.entity.Album;
import pojo.entity.Artist;
import pojo.entity.Track;
import java.util.ArrayList;
import java.util.List;

public class SpotifyResponse {
    public static Track parseTrack(JSONObject json) {
        String id = json.getString("id");
        String name = json.getString("name");
        // Parse album
        JSONObject albumJson = json.getJSONObject("album");
        Album album = parseAlbum(albumJson);
        // Parse artists
        JSONArray artistsArray = json.getJSONArray("artists");
        List<Album.SimplifiedArtist> artists = new ArrayList<>();
        for (int i = 0; i < artistsArray.length(); i++) {
            artists.add(parseSimplifiedArtist(artistsArray.getJSONObject(i)));
        }
        return Track.builder()
                .id(id)
                .name(name)
                .album(album)
                .artists(artists)
                .build();
    }

    public static Album parseAlbum(JSONObject json) {
        String id = json.getString("id");
        String name = json.getString("name");
        JSONArray artistsArray = json.getJSONArray("artists");
        List<Album.SimplifiedArtist> artists = new ArrayList<>();
        for (int i = 0; i < artistsArray.length(); i++) {
            artists.add(parseSimplifiedArtist(artistsArray.getJSONObject(i)));
        }
        return Album.builder()
                .id(id)
                .name(name)
                .artists(artists)
                .build();
    }

    public static Artist parseArtist(JSONObject json) {
        String id = json.getString("id");
        String name = json.getString("name");
        return Artist.builder()
                .id(id)
                .name(name)
                .build();
    }

    public static Album.SimplifiedArtist parseSimplifiedArtist(JSONObject json) {
        String id = json.getString("id");
        String name = json.getString("name");
        String href = json.optString("href", null);
        String type = json.optString("type", null);
        String uri = json.optString("uri", null);
        Artist.ExternalUrls externalUrls = null;
        if (json.has("external_urls")) {
            JSONObject extUrls = json.getJSONObject("external_urls");
            externalUrls = Artist.ExternalUrls.builder()
                    .spotify(extUrls.optString("spotify", null))
                    .build();
        }
        return Album.SimplifiedArtist.builder()
                .id(id)
                .name(name)
                .href(href)
                .type(type)
                .uri(uri)
                .externalUrls(externalUrls)
                .build();
    }
} 