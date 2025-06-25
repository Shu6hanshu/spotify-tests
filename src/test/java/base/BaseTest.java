package base;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

public class BaseTest {
    protected String accessToken;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://api.spotify.com/v1";
        // Ideally, read from env variable or config
        accessToken = System.getenv("SPOTIFY_ACCESS_TOKEN");
        if (accessToken == null) {
            accessToken = "YOUR_SPOTIFY_ACCESS_TOKEN"; // fallback placeholder
        }
    }
} 