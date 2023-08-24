package tests;

import enums.BrowserTypes;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import static testconstants.Constants.*;

public class SearchTermTests {
    public static WebDriver driver;
    public static WebDriverWait wait;

    @BeforeAll
    public static void setup()
    {
        driver = startBrowser(BrowserTypes.CHROME);
        driver.manage().window().maximize();
    }

    @BeforeEach
    public void waitAllElements()
    {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterAll
    public static void tearDown(){
        driver.close();
    }

    @Test
    public void resultFound_when_searchTermProvided_ByBingSearch(){

        driver.get(BING_URL);

        WebElement searchBox = driver.findElement(By.xpath("//input[@class='sb_form_q']"));
        searchBox.click();
        searchBox.sendKeys(SEARCH_TERM);
        searchBox.sendKeys(Keys.ENTER);

         wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
                (By.xpath("//h2[@class=' b_topTitle']")));
        WebElement firstResult = driver.findElement
                (By.xpath("//h2[@class=' b_topTitle']"));

        Assertions.assertTrue(firstResult.getText().contains(SEARCH_TERM), "Search result not found");
    }

    @Test
    public void resultFound_when_searchTermProvided_ByGoogleSearch(){

        driver.get(GOOGLE_URL);

        WebElement agreeButton = driver.findElement(By.xpath("//button[@id='L2AGLb']"));
        agreeButton.click();

        WebElement searchBox = driver.findElement(By.xpath("//textarea[@type='search']"));
        searchBox.click();
        searchBox.sendKeys(SEARCH_TERM);

        WebElement searchButton = driver.findElement(By.xpath("(//input[@type='submit' and @role='button'])[1]"));
        wait.until(ExpectedConditions.visibilityOf(searchButton));
        searchButton.click();

        WebElement firstResult = driver.findElement(By.xpath("(//div[@class='v7W49e'])[1]"));

        Assertions.assertTrue(firstResult.getText().contains(EXPECTED_TEXT), "Search result not found");
    }

    private static WebDriver startBrowser(BrowserTypes browserType)
    {
        switch (browserType){
            case EDGE:
                return new EdgeDriver();
            case EDGE_HEADLESS:
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--headless");
                return new EdgeDriver(edgeOptions);
            case CHROME:
                return  new ChromeDriver();
            case CHROME_HEADLESS:
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--headless");
                return new ChromeDriver(chromeOptions);
            case FIREFOX:
                return new FirefoxDriver();
            case FIREFOX_HEADLESS:
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--headless");
                return new FirefoxDriver(firefoxOptions);
            default:
                throw new IllegalArgumentException("The Browser Type is Undefined");
        }
    }
}
