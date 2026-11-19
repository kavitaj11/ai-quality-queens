package com.k11.pwj.support;

import com.k11.pwj.config.Config;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;

/**
 * One Playwright instance and one Browser per thread. A Playwright object is not thread-safe,
 * so parallel runs need one per thread; starting a browser is slow, so it is reused for every
 * test on that thread. Each test then gets its own cheap BrowserContext (see TestContext).
 */
public final class BrowserManager {

    private static final ThreadLocal<Playwright> PLAYWRIGHT = new ThreadLocal<>();
    private static final ThreadLocal<Browser> BROWSER = new ThreadLocal<>();

    private BrowserManager() {
    }

    public static Playwright playwright() {
        Playwright playwright = PLAYWRIGHT.get();
        if (playwright == null) {
            playwright = Playwright.create();
            PLAYWRIGHT.set(playwright);
            Playwright created = playwright;
            Runtime.getRuntime().addShutdownHook(new Thread(created::close));  // closes its browsers too
        }
        return playwright;
    }

    public static Browser browser() {
        Browser browser = BROWSER.get();
        if (browser == null || !browser.isConnected()) {
            browser = browserType().launch(new BrowserType.LaunchOptions()
                    .setHeadless(Config.headless())
                    .setSlowMo(Config.slowMoMillis()));
            BROWSER.set(browser);
        }
        return browser;
    }

    private static BrowserType browserType() {
        switch (Config.browser()) {
            case "firefox":
                return playwright().firefox();
            case "webkit":
                return playwright().webkit();
            case "chromium":
                return playwright().chromium();
            default:
                throw new IllegalArgumentException("Unsupported browser: " + Config.browser());
        }
    }
}
