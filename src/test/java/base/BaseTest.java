package base;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import util.SpotifyAuthorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    protected String accessToken;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.spotify.com/v1";
        // Try to get access token using authorization code flow
        try {
            logger.info("Attempting to refresh access token");
            accessToken = SpotifyAuthorization.refreshAccessToken();
        } catch (Exception e) {
            logger.warn("Failed to refresh access token programmatically: {}", e.getMessage());
            if (accessToken == null) {
                logger.error("No access token available. Please set SPOTIFY_REFRESH_TOKEN environment variable or configure SPOTIFY_CLIENT_ID and SPOTIFY_CLIENT_SECRET");
                throw new RuntimeException("Access token is required for tests. Please configure Spotify credentials.");
            }
        }
        logger.info("Access token obtained successfully");
    }
}