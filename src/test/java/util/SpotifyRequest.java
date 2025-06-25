package util;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import pojo.Album;
import pojo.Artist;
import pojo.Track;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;

import java.util.Map;
import java.util.HashMap;

public class SpotifyRequest {
    private static final Logger logger = LoggerFactory.getLogger(SpotifyRequest.class);
    public static Response sendRequest(String method, String endpoint, String accessToken, Map<String, Object> queryParams, Object body) {
        logger.info("Sending {} request to endpoint: {}", method, endpoint);
        if (queryParams != null && !queryParams.isEmpty()) {
            logger.info("Query params: {}", queryParams);
        }
        if (body != null) {
            logger.info("Request body: {}", body);
        }
        io.restassured.specification.RequestSpecification req = given()
                .header("Authorization", "Bearer " + accessToken);
        if (queryParams != null) {
            for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
                req = req.queryParam(entry.getKey(), entry.getValue());
            }
        }
        if (body != null) {
            req = req.contentType(ContentType.JSON).body(body instanceof String ? body : body.toString());
        }
        Response response;
        switch (method.toUpperCase()) {
            case "GET":
                response = req.get(endpoint);
                break;
            case "POST":
                response = req.post(endpoint);
                break;
            case "PUT":
                response = req.put(endpoint);
                break;
            case "DELETE":
                response = req.delete(endpoint);
                break;
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }
        logger.info("Response status code: {}", response.getStatusCode());
        return response;
    }

    public static Response searchTrack(String accessToken, String query) {
        Map<String, Object> params = new HashMap<>();
        params.put("q", query);
        params.put("type", "track");
        return sendRequest("GET", "/search", accessToken, params, null);
    }

    public static Response createPlaylist(String accessToken, String userId, String name, boolean isPublic) {
        JSONObject body = new JSONObject();
        body.put("name", name);
        body.put("public", isPublic);
        return sendRequest("POST", "/users/" + userId + "/playlists", accessToken, null, body);
    }

    public static Response addTracksToPlaylist(String accessToken, String playlistId, String[] trackUris) {
        JSONObject body = new JSONObject();
        JSONArray uris = new JSONArray();
        for (String uri : trackUris) {
            uris.put(uri);
        }
        body.put("uris", uris);
        return sendRequest("POST", "/playlists/" + playlistId + "/tracks", accessToken, null, body);
    }

    public static Response replaceTracksInPlaylist(String accessToken, String playlistId, String[] trackUris) {
        JSONObject body = new JSONObject();
        JSONArray uris = new JSONArray();
        for (String uri : trackUris) {
            uris.put(uri);
        }
        body.put("uris", uris);
        return sendRequest("PUT", "/playlists/" + playlistId + "/tracks", accessToken, null, body);
    }

    public static Response deletePlaylist(String accessToken, String playlistId) {
        return sendRequest("DELETE", "/playlists/" + playlistId + "/followers", accessToken, null, null);
    }
} 