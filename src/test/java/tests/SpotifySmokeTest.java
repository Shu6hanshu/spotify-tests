package tests;

import base.BaseTest;
import com.google.gson.Gson;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pojo.CreatePlaylistResponse;
import pojo.SearchTrackResponse;
import pojo.UserProfileResponse;
import pojo.entity.Album;
import util.SpotifyRequest;
import pojo.entity.Track;
import util.SpotifyAssertions;

public class SpotifySmokeTest extends BaseTest {
    private static Gson gson = new Gson();
    private static final Logger logger = LoggerFactory.getLogger(SpotifySmokeTest.class);
    private String userId;
    private String playlistIdShimla;
    private String playlistIdJaipur;

    // Track info as per Readme
    private static class TrackInfo {
        String trackId;
        String name;
        String artist;
        String album;
        TrackInfo(String trackId, String name, String artist, String album) {
            this.trackId = trackId;
            this.name = name;
            this.artist = artist;
            this.album = album;
        }
    }

    public static TrackInfo[] trackInfo = new TrackInfo[] {
        new TrackInfo(null, "Zinda", null, "Bhaag Milkha Bhaag"),
        new TrackInfo(null, "Give me some Sunshine", null, "3 Idiots"),
        new TrackInfo(null, "In The End", "Linkin Park", null),
        new TrackInfo(null, "Yun Hi Chala Chal", null, "Swades"),
        new TrackInfo(null, "Khalasi", null, null),
        new TrackInfo(null, "Kya Jaipur Kya Dilli", "Rahgir", null),
        new TrackInfo(null, "Senorita", null, "Zindagi Na Milegi Dobara")
    };

    @DataProvider(name = "trackData")
    public static TrackInfo[] trackData() {
        return trackInfo;
    }

    private String searchTrackId(TrackInfo track) {
        StringBuilder query = new StringBuilder();
        query.append("track:").append(track.name);
        if (track.artist != null) {
            query.append(" artist:").append(track.artist);
        }
        if (track.album != null) {
            query.append(" album:").append(track.album);
        }

        logger.info("Searching for track: {} , Query: {}", track.name, query);
        Response response = SpotifyRequest.searchTrack(accessToken, query.toString());
        logger.info("Search response status: {}", response.getStatusCode());

        // Deserialize the 'tracks' object from the response into the Tracks POJO
        SearchTrackResponse searchTrackResponse = gson.fromJson(new JSONObject(response.getBody().asString()).getJSONObject("tracks").toString(), SearchTrackResponse.class);
        logger.info("Number of items found: {}", searchTrackResponse.getTotal());
        if (searchTrackResponse.getItems() == null || searchTrackResponse.getItems().isEmpty()) {
            SpotifyAssertions.assertValue(false, true, "No results for track: " + track.name, SpotifyAssertions.AssertionType.EQUALS);
            return null;
        }

        Track parsedTrack = searchTrackResponse.getItems().get(0);
        logger.info("Found track id for {}: {}", track.name, parsedTrack.getId());

        Album.SimplifiedArtist parsedArtist = searchTrackResponse.getItems().get(0).getArtists().get(0);
        logger.info("Found Artist Name for {}: {}", track.name, parsedArtist.getName());

        Album parsedAlbum = searchTrackResponse.getItems().get(0).getAlbum();
        logger.info("Found Album Name for {}: {}", track.name, parsedAlbum.getName());

        if (track.name!=null) {
            SpotifyAssertions.assertTrack(parsedTrack, track.name);
        }
        if (track.artist!=null) {
            SpotifyAssertions.assertArtist(parsedArtist, track.artist);
        }
        if (track.album!=null) {
            SpotifyAssertions.assertAlbum(parsedAlbum, track.album);
        }

        return parsedTrack.getId();
    }

    @Test(priority = 0, dataProvider = "trackData")
    public void fetchTrackIds(TrackInfo track) {
        logger.info("Fetching track ID for: {}", track.name);
        String id = searchTrackId(track);
        logger.info("Track {} ({}) has id: {}", track.name, track.name, id);
        // You may want to store or assert the id here as needed
        track.trackId=id;
        Assert.assertNotNull(id, "Track ID should not be null for " + track.name);
    }

    @Test(priority = 1,dependsOnMethods = "fetchTrackIds")
    public void getCurrentUserId() {
        logger.info("Getting current user id...");
        Response response = SpotifyRequest.sendRequest("GET", "/me", accessToken, null, null,200);
        logger.info("/me response status: {}", response.getStatusCode());
         // Deserialize the user profile response
        UserProfileResponse userProfileResponse = gson.fromJson(response.getBody().asString(), UserProfileResponse.class);
        userId = userProfileResponse.getId();
        logger.info("Current user id: {}", userId);
        Assert.assertNotNull(userId);
    }

    @Test(priority = 2,dependsOnMethods = "getCurrentUserId")
    public void createPlaylistShimla() {
        logger.info("Creating playlist: Road Trip Shimla");
        Response response = SpotifyRequest.createPlaylist(accessToken, userId, "Road Trip Shimla", false);
        logger.info("Create playlist response status: {}", response.getStatusCode());
        CreatePlaylistResponse createPlaylistResponse = gson.fromJson(response.getBody().asString(), CreatePlaylistResponse.class);
        playlistIdShimla = createPlaylistResponse.getId();
        logger.info("Created playlistIdShimla: {}", playlistIdShimla);
        SpotifyAssertions.assertValue(playlistIdShimla, null, "playlistIdShimla should not be null", SpotifyAssertions.AssertionType.NOT_NULL);
        SpotifyAssertions.assertValue(response.jsonPath().getString("name"), "Road Trip Shimla", "Playlist name mismatch", SpotifyAssertions.AssertionType.EQUALS);
    }

    @Test(priority = 3, dependsOnMethods = "createPlaylistShimla")
    public void addTracksToShimla() {
        logger.info("Adding tracks to Shimla playlist: {}", playlistIdShimla);
        String[] uris = new String[] {
            "spotify:track:" + trackInfo[0].trackId,
            "spotify:track:" + trackInfo[1].trackId,
            "spotify:track:" + trackInfo[2].trackId
        };
        logger.info("Tracks being added: {}", (Object) uris);
        Response response = SpotifyRequest.addTracksToPlaylist(accessToken, playlistIdShimla, uris);
        logger.info("Add tracks response status: {}", response.getStatusCode());
    }

    @Test(priority = 4, dependsOnMethods = "addTracksToShimla")
    public void replaceTrackCwithD() {
        logger.info("Replacing track C with D in Shimla playlist: {}", playlistIdShimla);
        String[] uris = new String[] {
                "spotify:track:" + trackInfo[0].trackId,
                "spotify:track:" + trackInfo[1].trackId,
                "spotify:track:" + trackInfo[3].trackId
        };
        logger.info("Tracks after replacement: {}", (Object) uris);
        Response response = SpotifyRequest.replaceTracksInPlaylist(accessToken, playlistIdShimla, uris);
        logger.info("Replace tracks response status: {}", response.getStatusCode());
    }

    @Test(priority = 5, dependsOnMethods = "replaceTrackCwithD")
    public void createPlaylistJaipur() {
        logger.info("Creating playlist: Road Trip Jaipur");
        Response response = SpotifyRequest.createPlaylist(accessToken, userId, "Road Trip Jaipur", false);
        logger.info("Create playlist response status: {}", response.getStatusCode());
        CreatePlaylistResponse createPlaylistResponse = gson.fromJson(response.getBody().asString(), CreatePlaylistResponse.class);
        playlistIdJaipur = createPlaylistResponse.getId();
        logger.info("Created playlistIdJaipur: {}", playlistIdJaipur);
        SpotifyAssertions.assertValue(playlistIdJaipur, null, "playlistIdJaipur should not be null", SpotifyAssertions.AssertionType.NOT_NULL);
        SpotifyAssertions.assertValue(response.jsonPath().getString("name"), "Road Trip Jaipur", "Playlist name mismatch", SpotifyAssertions.AssertionType.EQUALS);
    }

    @Test(priority = 6, dependsOnMethods = "createPlaylistJaipur")
    public void addTracksToJaipur() {
        logger.info("Adding tracks to Jaipur playlist: {}", playlistIdJaipur);
        String[] uris = new String[] {
                "spotify:track:" + trackInfo[4].trackId,
                "spotify:track:" + trackInfo[5].trackId,
                "spotify:track:" + trackInfo[6].trackId
        };
        logger.info("Tracks being added: {}", (Object) uris);
        Response response = SpotifyRequest.addTracksToPlaylist(accessToken, playlistIdJaipur, uris);
        logger.info("Add tracks response status: {}", response.getStatusCode());
    }

    @Test(priority = 99, dependsOnMethods = {"addTracksToShimla", "addTracksToJaipur"}, alwaysRun = true)
    public void cleanup() {
        logger.info("Cleaning up playlists...");
        if (playlistIdShimla != null) {
            logger.info("Deleting playlistIdShimla: {}", playlistIdShimla);
            Response response = SpotifyRequest.deletePlaylist(accessToken, playlistIdShimla);
            logger.info("Delete Shimla playlist response status: {}", response.getStatusCode());
        }
        if (playlistIdJaipur != null) {
            logger.info("Deleting playlistIdJaipur: {}", playlistIdJaipur);
            Response response = SpotifyRequest.deletePlaylist(accessToken, playlistIdJaipur);
            logger.info("Delete Jaipur playlist response status: {}", response.getStatusCode());
        }
    }
} 