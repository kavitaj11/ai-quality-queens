package com.k11.shop.bdd;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/** Runs the .feature files through TestNG. Filter scenarios with -Dcucumber.filter.tags="@smoke". */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.k11.shop.bdd",
        plugin = {"pretty", "html:target/cucumber-report.html", "json:target/cucumber.json"})
public class CucumberRunner extends AbstractTestNGCucumberTests {
}
