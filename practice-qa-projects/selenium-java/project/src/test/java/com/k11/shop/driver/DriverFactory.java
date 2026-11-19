package com.k11.shop.driver;

import com.k11.shop.config.Config;
import java.net.MalformedURLException;
import java.net.URI;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Creates and holds one WebDriver per thread. ThreadLocal is what makes parallel test
 * execution safe: each TestNG thread gets its own browser and never touches another's.
 *
 * Selenium Manager (built into Selenium 4.6+) finds or downloads the matching driver
 * binary (chromedriver, msedgedriver, geckodriver), so no WebDriverManager is needed.
 */
public final class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("No WebDriver for this thread. Call DriverFactory.start() first.");
        }
        return driver;
    }

    public static WebDriver start() {
        WebDriver driver = Config.gridUrl().isEmpty() ? local(options()) : remote(options());
        // No implicit wait on purpose: all waiting is explicit (see BasePage).
        driver.manage().window().setSize(new Dimension(1280, 900));
        DRIVER.set(driver);
        return driver;
    }

    public static void quit() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                DRIVER.remove();  // avoid leaking a dead driver into the next test on this thread
            }
        }
    }

    private static Capabilities options() {
        boolean headless = Config.headless();
        switch (Config.browser()) {
            case "edge": {
                EdgeOptions o = new EdgeOptions();
                if (headless) {
                    o.addArguments("--headless=new");
                }
                return o;
            }
            case "firefox": {
                FirefoxOptions o = new FirefoxOptions();
                if (headless) {
                    o.addArguments("-headless");
                }
                return o;
            }
            case "chrome": {
                ChromeOptions o = new ChromeOptions();
                if (headless) {
                    o.addArguments("--headless=new");
                }
                o.addArguments("--disable-search-engine-choice-screen");
                return o;
            }
            default:
                throw new IllegalArgumentException("Unsupported browser: " + Config.browser());
        }
    }

    private static WebDriver local(Capabilities caps) {
        if (caps instanceof EdgeOptions) {
            return new EdgeDriver((EdgeOptions) caps);
        }
        if (caps instanceof FirefoxOptions) {
            return new FirefoxDriver((FirefoxOptions) caps);
        }
        return new ChromeDriver((ChromeOptions) caps);
    }

    private static WebDriver remote(Capabilities caps) {
        try {
            return new RemoteWebDriver(URI.create(Config.gridUrl()).toURL(), caps);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Bad gridUrl: " + Config.gridUrl(), e);
        }
    }
}
