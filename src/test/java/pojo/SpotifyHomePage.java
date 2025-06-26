package pojo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SpotifyHomePage {
    private final WebDriver driver;

    public SpotifyHomePage(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement getLogo() {
        return driver.findElement(By.cssSelector("a[href='/'] svg"));
    }

    public WebElement getSearchInput() {
        return driver.findElement(By.cssSelector("input[placeholder*='What do you want to play']"));
    }

    public boolean isTrendingSongsSectionPresent() {
        return !driver.findElements(By.cssSelector("[aria-label='Trending songs']")).isEmpty();
    }

    public boolean isPopularArtistsSectionPresent() {
        return !driver.findElements(By.cssSelector("[aria-label='Popular artists']")).isEmpty();
    }

    public boolean isPopularAlbumsSectionPresent() {
        return !driver.findElements(By.cssSelector("[aria-label='Popular albums and singles']")).isEmpty();
    }

    public boolean isSignUpButtonPresent() {
        return !driver.findElements(By.cssSelector("[data-testid='signup-button']")).isEmpty();
    }

    public boolean isLoginButtonPresent() {
        return !driver.findElements(By.cssSelector("[data-testid='login-button']")).isEmpty();
    }
} 