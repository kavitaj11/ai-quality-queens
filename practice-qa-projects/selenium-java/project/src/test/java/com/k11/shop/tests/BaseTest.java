package com.k11.shop.tests;

import com.k11.shop.driver.DriverFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * A fresh browser per test method: slower than sharing one, but tests can never leak state.
 *
 * Do NOT store the driver in a field here. With parallel="methods", TestNG runs many methods
 * of ONE test-class instance on different threads, so a field would be overwritten by other
 * threads mid-test. Always read it from the ThreadLocal through driver().
 */
public abstract class BaseTest {

    protected WebDriver driver() {
        return DriverFactory.getDriver();
    }

    @BeforeMethod(alwaysRun = true)
    public void startBrowser() {
        DriverFactory.start();
    }

    @AfterMethod(alwaysRun = true)
    public void stopBrowser(ITestResult result) {
        try {
            if (result.getStatus() == ITestResult.FAILURE) {
                saveScreenshot(result.getMethod().getMethodName());
            }
        } finally {
            DriverFactory.quit();
        }
    }

    /** Screenshot on failure: the first thing anyone asks for when a CI test fails. */
    private void saveScreenshot(String testName) {
        try {
            Path dir = Paths.get("target", "screenshots");
            Files.createDirectories(dir);
            Path source = ((TakesScreenshot) driver()).getScreenshotAs(OutputType.FILE).toPath();
            Path target = dir.resolve(testName + "-" + UUID.randomUUID().toString().substring(0, 8) + ".png");
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Screenshot saved: " + target.toAbsolutePath());
        } catch (IOException | RuntimeException e) {
            System.out.println("Could not save screenshot: " + e.getMessage());
        }
    }
}
