package tests;

import base.BaseUiTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import org.testng.asserts.SoftAssert;
import pojo.SpotifyHomePage;
import pojo.SpotifySearchPage;

public class SpotifyUiTest extends BaseUiTest {
    private static final Logger logger = LoggerFactory.getLogger(SpotifyUiTest.class);

    @Test
    public void testSpotifyHomePageElements() {
        logger.info("Starting test: testSpotifyHomePageElements");
        SoftAssert softAssert = new SoftAssert();
        driver.get("https://open.spotify.com/");
        // Wait for page load complete
        new WebDriverWait(driver, Duration.ofSeconds(30)).until(
            webDriver -> ((String)((org.openqa.selenium.JavascriptExecutor) webDriver)
                .executeScript("return document.readyState")).equals("complete")
        );
        // Wait for 3 seconds for dynamic content to load
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            logger.warn("Sleep interrupted", e);
            Thread.currentThread().interrupt();
        }

        SpotifyHomePage homePage = new SpotifyHomePage(driver);

        // 1. Spotify logo (top left)
        try {
            softAssert.assertTrue(homePage.getLogo().isDisplayed(), "Spotify logo should be visible on the top left");
            logger.info("Spotify logo is visible on the top left");
        } catch (Exception e) {
            logger.error("Spotify logo not found or not visible", e);
            softAssert.fail("Spotify logo not found or not visible");
        }

        // 2. Search window
        try {
            softAssert.assertTrue(homePage.getSearchInput().isDisplayed(), "Search window should be visible");
            logger.info("Search window is visible");
        } catch (Exception e) {
            logger.error("Search window not found or not visible", e);
            softAssert.fail("Search window not found or not visible");
        }

        // 3. Trending songs section (aria-label)
        boolean trendingSongsFound = homePage.isTrendingSongsSectionPresent();
        softAssert.assertTrue(trendingSongsFound, "Trending songs section (aria-label) should be present");
        logger.info("Trending songs section (aria-label) is present: {}", trendingSongsFound);

        // 4. Popular artists section (aria-label)
        boolean popularArtistsFound = homePage.isPopularArtistsSectionPresent();
        softAssert.assertTrue(popularArtistsFound, "Popular artists section (aria-label) should be present");
        logger.info("Popular artists section (aria-label) is present: {}", popularArtistsFound);

        // 5. Popular albums section (aria-label)
        boolean popularAlbumsFound = homePage.isPopularAlbumsSectionPresent();
        softAssert.assertTrue(popularAlbumsFound, "Popular albums and singles section (aria-label) should be present");
        logger.info("Popular albums and singles section (aria-label) is present: {}", popularAlbumsFound);

        // 6. Sign up and Log in links (data-testid)
        boolean signUpFound = homePage.isSignUpButtonPresent();
        boolean logInFound = homePage.isLoginButtonPresent();
        softAssert.assertTrue(signUpFound, "Sign up button (data-testid) should be present on the top right");
        logger.info("Sign up button (data-testid) is present: {}", signUpFound);
        softAssert.assertTrue(logInFound, "Log in button (data-testid) should be present on the top right");
        logger.info("Log in button (data-testid) is present: {}", logInFound);

        softAssert.assertAll();
    }

    @DataProvider(name = "songSearchData")
    public Object[][] songSearchData() {
        return new Object[][] {
            {"Zinda", "Bhaag Milkha Bhaag", true},
            {"In The End", "Linkin Park", true},
            {"Diamonds", "Linkin Park", false}
        };
    }

    @Test(dataProvider = "songSearchData", dependsOnMethods = "testSpotifyHomePageElements")
    public void testSearchForSong(String songName, String artistOrMovie, boolean shouldBeFound) {
        logger.info("Starting test: testSearchForSong - {} by {} (shouldBeFound: {})", songName, artistOrMovie, shouldBeFound);
        SoftAssert softAssert = new SoftAssert();
        driver.get("https://open.spotify.com/");
        // Wait for page load complete
        new WebDriverWait(driver, Duration.ofSeconds(30)).until(
            webDriver -> ((String)((org.openqa.selenium.JavascriptExecutor) webDriver)
                .executeScript("return document.readyState")).equals("complete")
        );
        // Wait for 3 seconds for dynamic content to load
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            logger.warn("Sleep interrupted", e);
            Thread.currentThread().interrupt();
        }

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        SpotifySearchPage searchPage = new SpotifySearchPage(driver);

        // Find and interact with the search input
        try {
            WebElement searchInput = searchPage.getSearchInput();
            softAssert.assertTrue(searchInput.isDisplayed(), "Search window should be visible");
            logger.info("Search window is visible");
            searchPage.searchFor(songName + " " + artistOrMovie);
            logger.info("Typed '{}' into search input", songName + " " + artistOrMovie);
        } catch (Exception e) {
            logger.error("Search input not found or not visible", e.getStackTrace());
            softAssert.fail("Search input not found or not visible");
            softAssert.assertAll();
            return;
        }

        // Wait for search results and verify song and artist/movie name in tracklist row (case-insensitive)
        boolean songFound = false;
        try {
            // Wait for at least one tracklist row to appear
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-testid='tracklist-row']")));
            // Get all tracklist rows
            java.util.List<org.openqa.selenium.WebElement> rows = searchPage.getTracklistRows();
            songFound = rows.stream().anyMatch(row -> {
                String text = row.getText().toLowerCase();
                return text.contains(songName.toLowerCase()) && text.contains(artistOrMovie.toLowerCase());
            });
        } catch (Exception e) {
            logger.error("Could not find search result for '{}' by '{}' in tracklist rows (case-insensitive)", songName, artistOrMovie, e);
        }
        softAssert.assertEquals(songFound, shouldBeFound, String.format("Search result for '%s' by '%s' expected: %s, found: %s", songName, artistOrMovie, shouldBeFound, songFound));
        logger.info("Search result for '{}' by '{}' is as expected in tracklist rows (case-insensitive): {} (expected: {})", songName, artistOrMovie, songFound, shouldBeFound);
        softAssert.assertAll();
    }
} 