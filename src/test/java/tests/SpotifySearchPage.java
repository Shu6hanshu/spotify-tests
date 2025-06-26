package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpotifySearchPage {
    private static final Logger logger = LoggerFactory.getLogger(SpotifySearchPage.class);
    private final WebDriver driver;

    public SpotifySearchPage(WebDriver driver) {
        this.driver = driver;
        logger.debug("SpotifySearchPage initialized with driver: {}", driver);
    }

    public WebElement getSearchInput() {
        logger.info("Locating search input element");
        WebElement element = driver.findElement(By.cssSelector("input[placeholder*='What do you want to play']"));
        logger.info("Search input element found: {}", element != null);
        return element;
    }

    public void searchFor(String query) {
        logger.info("Searching for query: '{}'", query);
        WebElement searchInput = getSearchInput();
        searchInput.click();
        logger.debug("Clicked on search input");
        searchInput.clear();
        logger.debug("Cleared search input");
        searchInput.sendKeys(query);
        logger.debug("Typed query into search input: '{}'", query);
        searchInput.sendKeys(Keys.ENTER);
        logger.info("Pressed ENTER to submit search");
    }

    public List<WebElement> getTracklistRows() {
        logger.debug("Fetching tracklist rows");
        List<WebElement> rows = driver.findElements(By.cssSelector("[data-testid='tracklist-row']"));
        logger.info("Number of tracklist rows found: {}", rows.size());
        return rows;
    }
} 