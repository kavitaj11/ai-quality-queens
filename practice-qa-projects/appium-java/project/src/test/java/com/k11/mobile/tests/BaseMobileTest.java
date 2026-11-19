package com.k11.mobile.tests;

import com.k11.mobile.driver.DriverFactory;
import io.appium.java_client.AppiumDriver;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.openqa.selenium.OutputType;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/** One Appium session per test; screenshot on failure. The driver is never stored in a field. */
public abstract class BaseMobileTest {

    protected AppiumDriver driver() {
        return DriverFactory.getDriver();
    }

    @BeforeMethod(alwaysRun = true)
    public void startSession() {
        DriverFactory.start();
    }

    @AfterMethod(alwaysRun = true)
    public void endSession(ITestResult result) {
        try {
            if (result.getStatus() == ITestResult.FAILURE) {
                Path dir = Paths.get("target", "screenshots");
                Files.createDirectories(dir);
                Path png = driver().getScreenshotAs(OutputType.FILE).toPath();
                Files.copy(png, dir.resolve(result.getMethod().getMethodName() + ".png"),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException | RuntimeException e) {
            System.out.println("Could not save screenshot: " + e.getMessage());
        } finally {
            DriverFactory.quit();
        }
    }
}
