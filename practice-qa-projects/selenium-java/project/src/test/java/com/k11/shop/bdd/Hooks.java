package com.k11.shop.bdd;

import com.k11.shop.driver.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

/** One browser per scenario, and a screenshot attached to the report when a scenario fails. */
public class Hooks {

    @Before
    public void startBrowser() {
        DriverFactory.start();
    }

    @After
    public void stopBrowser(Scenario scenario) {
        try {
            if (scenario.isFailed()) {
                byte[] png = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.BYTES);
                scenario.attach(png, "image/png", scenario.getName());
            }
        } finally {
            DriverFactory.quit();
        }
    }
}
