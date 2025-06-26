package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class BaseUiTest {
    protected WebDriver driver;
    private static final Logger logger = LoggerFactory.getLogger(BaseUiTest.class);

    @BeforeClass
    public void setUpDriver() {
        logger.info("Setting up ChromeDriver for UI tests");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        logger.info("ChromeDriver setup complete");
    }

    @AfterClass
    public void tearDownDriver() {
        logger.info("Tearing down ChromeDriver");
        if (driver != null) {
            driver.quit();
            logger.info("ChromeDriver quit successfully");
        }
    }
} 