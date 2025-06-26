package util;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import com.google.gson.*;
import pojo.AddTracksRequest;
import pojo.CreatePlaylistRequest;
import pojo.ReplaceTracksRequest;
import pojo.SearchTrackRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import static io.restassured.RestAssured.given;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

public class SpotifyRequest {
    private static Gson gson = new Gson();
    private static final Logger logger = LoggerFactory.getLogger(SpotifyRequest.class);
    public static Response sendRequest(String method, String endpoint, String accessToken, Map<String, Object> queryParams, Object body, Integer expectedStatusCode) {
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
        SpotifyAssertions.assertStatusCode(response.getStatusCode(), expectedStatusCode);
        logger.info("Response status code: {}", response.getStatusCode());
        return response;
    }

    public static Map<String, Object> toParamMap(String json) {
        Type type = new TypeToken<HashMap<String, Object>>(){}.getType();
        HashMap<String, Object> params = gson.fromJson(json, type);
        return params;
    }

    public static Response searchTrack(String accessToken, String query) {
        SearchTrackRequest searchTrackRequest = SearchTrackRequest.builder()
                .q(query)
                .type("track")
                .build();
        String json = gson.toJson(searchTrackRequest);                
        Map<String, Object> params = toParamMap(json);
        return sendRequest("GET", "/search", accessToken, params, null,200);
    }

    public static Response createPlaylist(String accessToken, String userId, String name, boolean isPublic) {
        CreatePlaylistRequest createPlaylistRequest = CreatePlaylistRequest.builder()
                .name(name)
                .isPublic(isPublic)
                .build();
        String body = gson.toJson(createPlaylistRequest);
        return sendRequest("POST", "/users/" + userId + "/playlists", accessToken, null, body,201);
    }

    public static Response addTracksToPlaylist(String accessToken, String playlistId, String[] trackUris) {
        AddTracksRequest addTracksRequest = AddTracksRequest.builder()
                .uris(Arrays.asList(trackUris))
                .build();
        String body = gson.toJson(addTracksRequest);
        return sendRequest("POST", "/playlists/" + playlistId + "/tracks", accessToken, null, body,201);
    }

    public static Response replaceTracksInPlaylist(String accessToken, String playlistId, String[] trackUris) {
        ReplaceTracksRequest replaceTracksRequest = ReplaceTracksRequest.builder()
        .uris(Arrays.asList(trackUris))
        .build();
        String body = gson.toJson(replaceTracksRequest);
        return sendRequest("PUT", "/playlists/" + playlistId + "/tracks", accessToken, null, body,200);
    }

    public static Response deletePlaylist(String accessToken, String playlistId) {
        return sendRequest("DELETE", "/playlists/" + playlistId + "/followers", accessToken, null, null,200);
    }
} 