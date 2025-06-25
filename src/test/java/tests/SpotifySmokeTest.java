package tests;

import base.BaseTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.Reporter;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import util.SpotifyRequest;
import util.SpotifyResponse;
import pojo.Track;

public class SpotifySmokeTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(SpotifySmokeTest.class);
    private String userId;
    private String playlistIdShimla;
    private String playlistIdJaipur;

    // Track IDs will be populated before tests
    private String trackA;
    private String trackB;
    private String trackC;
    private String trackD;
    private String trackE;
    private String trackF;
    private String trackG;

    // Track info as per Readme
    private static class TrackInfo {
        String key;
        String name;
        String artist;
        String album;
        TrackInfo(String key, String name, String artist, String album) {
            this.key = key;
            this.name = name;
            this.artist = artist;
            this.album = album;
        }
    }

    @DataProvider(name = "trackData")
    public Object[][] trackData() {
        return new Object[][]{
                {new TrackInfo("A", "Zinda", null, "Bhaag Milkha Bhaag")},
                {new TrackInfo("B", "Give me some Sunshine", null, "3 Idiots")},
                {new TrackInfo("C", "In The End", "Linkin Park", null)},
                {new TrackInfo("D", "Yun Hi Chala Chal", null, "Swades")},
                {new TrackInfo("E", "Khalasi", null, null)},
                {new TrackInfo("F", "Kya Jaipur Kya Dilli", "Rahgir", null)},
                {new TrackInfo("G", "Senorita", null, "Zindagi Na Milegi Dobara")}
        };
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
        Assert.assertEquals(response.getStatusCode(), 200, "Failed to search for track: " + track.name);
        JSONObject json = new JSONObject(response.getBody().asString());
        JSONArray items = json.getJSONObject("tracks").getJSONArray("items");
        logger.info("Number of items found: {}", items.length());
        Assert.assertTrue(items.length() > 0, "No results for track: " + track.name);
        // Use parser for Track
        JSONObject trackJson = items.getJSONObject(0);
        Track parsedTrack = SpotifyResponse.parseTrack(trackJson);
        logger.info("Found track id for {}: {}", track.name, parsedTrack.getId());
        return parsedTrack.getId();
    }

    @BeforeClass
    public void fetchTrackIds() {
        logger.info("Fetching track IDs for all tracks...");
        Map<String, String> trackIdMap = new HashMap<>();
        Object[][] tracks = trackData();
        for (Object[] row : tracks) {
            TrackInfo track = (TrackInfo) row[0];
            String id = searchTrackId(track);
            logger.info("Track {} ({}) has id: {}", track.key, track.name, id);
            trackIdMap.put(track.key, id);
        }
        trackA = trackIdMap.get("A");
        trackB = trackIdMap.get("B");
        trackC = trackIdMap.get("C");
        trackD = trackIdMap.get("D");
        trackE = trackIdMap.get("E");
        trackF = trackIdMap.get("F");
        trackG = trackIdMap.get("G");
        logger.info("Track IDs fetched: {}", trackIdMap);
    }

    @Test(priority = 1)
    public void getCurrentUserId() {
        logger.info("Getting current user id...");
        Response response = SpotifyRequest.sendRequest("GET", "/me", accessToken, null, null);
        logger.info("/me response status: {}", response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(), 200);
        userId = response.jsonPath().getString("id");
        logger.info("Current user id: {}", userId);
        Assert.assertNotNull(userId);
    }

    @Test(priority = 2, dependsOnMethods = "getCurrentUserId")
    public void createPlaylistShimla() {
        logger.info("Creating playlist: Road Trip Shimla");
        JSONObject body = new JSONObject();
        body.put("name", "Road Trip Shimla");
        body.put("public", false);
        Response response = SpotifyRequest.createPlaylist(accessToken, userId, "Road Trip Shimla", false);
        logger.info("Create playlist response status: {}", response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(), 201);
        playlistIdShimla = response.jsonPath().getString("id");
        logger.info("Created playlistIdShimla: {}", playlistIdShimla);
        Assert.assertNotNull(playlistIdShimla);
        Assert.assertEquals(response.jsonPath().getString("name"), "Road Trip Shimla");
    }

    @Test(priority = 3, dependsOnMethods = "createPlaylistShimla")
    public void addTracksToShimla() {
        logger.info("Adding tracks to Shimla playlist: {}", playlistIdShimla);
        String[] uris = new String[] {
            "spotify:track:" + trackA,
            "spotify:track:" + trackB,
            "spotify:track:" + trackC
        };
        logger.info("Tracks being added: {}", (Object) uris);
        Response response = SpotifyRequest.addTracksToPlaylist(accessToken, playlistIdShimla, uris);
        logger.info("Add tracks response status: {}", response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(), 201);
    }

    @Test(priority = 4, dependsOnMethods = "addTracksToShimla")
    public void replaceTrackCwithD() {
        logger.info("Replacing track C with D in Shimla playlist: {}", playlistIdShimla);
        String[] uris = new String[] {
            "spotify:track:" + trackA,
            "spotify:track:" + trackB,
            "spotify:track:" + trackD
        };
        logger.info("Tracks after replacement: {}", (Object) uris);
        Response response = SpotifyRequest.replaceTracksInPlaylist(accessToken, playlistIdShimla, uris);
        logger.info("Replace tracks response status: {}", response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(), 201);
    }

    @Test(priority = 5, dependsOnMethods = "replaceTrackCwithD")
    public void createPlaylistJaipur() {
        logger.info("Creating playlist: Road Trip Jaipur");
        Response response = SpotifyRequest.createPlaylist(accessToken, userId, "Road Trip Jaipur", false);
        logger.info("Create playlist response status: {}", response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(), 201);
        playlistIdJaipur = response.jsonPath().getString("id");
        logger.info("Created playlistIdJaipur: {}", playlistIdJaipur);
        Assert.assertNotNull(playlistIdJaipur);
        Assert.assertEquals(response.jsonPath().getString("name"), "Road Trip Jaipur");
    }

    @Test(priority = 6, dependsOnMethods = "createPlaylistJaipur")
    public void addTracksToJaipur() {
        logger.info("Adding tracks to Jaipur playlist: {}", playlistIdJaipur);
        String[] uris = new String[] {
            "spotify:track:" + trackE,
            "spotify:track:" + trackF,
            "spotify:track:" + trackG
        };
        logger.info("Tracks being added: {}", (Object) uris);
        Response response = SpotifyRequest.addTracksToPlaylist(accessToken, playlistIdJaipur, uris);
        logger.info("Add tracks response status: {}", response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(), 201);
    }

    @Test(priority = 99, dependsOnMethods = {"addTracksToShimla", "addTracksToJaipur"}, alwaysRun = true)
    public void cleanup() {
        logger.info("Cleaning up playlists...");
        if (playlistIdShimla != null) {
            logger.info("Deleting playlistIdShimla: {}", playlistIdShimla);
            Response response = SpotifyRequest.deletePlaylist(accessToken, playlistIdShimla);
            logger.info("Delete Shimla playlist response status: {}", response.getStatusCode());
            Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 202);
        }
        if (playlistIdJaipur != null) {
            logger.info("Deleting playlistIdJaipur: {}", playlistIdJaipur);
            Response response = SpotifyRequest.deletePlaylist(accessToken, playlistIdJaipur);
            logger.info("Delete Jaipur playlist response status: {}", response.getStatusCode());
            Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 202);
        }
    }
} 