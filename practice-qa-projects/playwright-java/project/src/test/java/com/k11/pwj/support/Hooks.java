package com.k11.pwj.support;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

/**
 * Cucumber hooks: run around every scenario. They receive the same TestContext the step
 * definitions get (PicoContainer injects it through the constructor).
 */
public class Hooks {

    private final TestContext ctx;

    public Hooks(TestContext ctx) {
        this.ctx = ctx;
    }

    @Before
    public void startSession(Scenario scenario) {
        ctx.start(scenario.getName());
    }

    /** On failure: screenshot attached to the Cucumber report, trace.zip saved to target/. */
    @After
    public void endSession(Scenario scenario) {
        byte[] screenshot = ctx.finish(scenario.isFailed());
        if (screenshot != null) {
            scenario.attach(screenshot, "image/png", scenario.getName());
        }
    }
}
