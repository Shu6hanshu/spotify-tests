package util;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class SpotifyAuthorization {
    private static final Logger logger = LoggerFactory.getLogger(SpotifyAuthorization.class);
    
    // Spotify API credentials - these should be configured via environment variables
    private static final String CLIENT_ID = System.getenv("SPOTIFY_CLIENT_ID");
    private static final String CLIENT_SECRET = System.getenv("SPOTIFY_CLIENT_SECRET");
    private static final String REFRESH_TOKEN = System.getenv("SPOTIFY_REFRESH_TOKEN");
    
    // Authorization endpoints
    private static final String TOKEN_URL = "https://accounts.spotify.com/api/token";
        
    // Method to refresh access token
    public static String refreshAccessToken() {
        logger.info("Refreshing access token...");
        
        if (CLIENT_ID == null || CLIENT_SECRET == null) {
            throw new RuntimeException("SPOTIFY_CLIENT_ID and SPOTIFY_CLIENT_SECRET environment variables must be set");
        }
        
        // Create basic auth header
        String credentials = CLIENT_ID + ":" + CLIENT_SECRET;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        
        // Prepare request body
        Map<String, String> formData = new HashMap<>();
        formData.put("grant_type", "refresh_token");
        formData.put("refresh_token", REFRESH_TOKEN);
        
        Response response = given()
                .header("Authorization", "Basic " + encodedCredentials)
                .contentType(ContentType.URLENC)
                .formParams(formData)
                .post(TOKEN_URL);
        
        if (response.getStatusCode() != 200) {
            logger.error("Token refresh failed. Status: {}, Body: {}", 
                        response.getStatusCode(), response.getBody().asString());
            throw new RuntimeException("Failed to refresh access token");
        }
        
        JSONObject tokenResponse = new JSONObject(response.getBody().asString());
        return tokenResponse.getString("access_token");
    }
} 