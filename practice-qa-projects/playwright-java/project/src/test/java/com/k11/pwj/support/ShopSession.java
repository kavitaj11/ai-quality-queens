package com.k11.pwj.support;

import com.k11.pwj.config.Config;
import com.k11.pwj.services.ShopApi;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Tracing;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Everything one test (or one Cucumber scenario) needs, created fresh and thrown away after:
 *  - an isolated BrowserContext (its own cookies and storage) and a Page,
 *  - ui()  : the API service object over context.request(), which SHARES the page's cookies,
 *  - newApiClient(): standalone API clients for pure API tests (each is a separate user),
 *  - a trace of the whole test, kept only if the test failed.
 */
public class ShopSession {

    private final String name;
    private final BrowserContext context;
    private final Page page;
    private final List<APIRequestContext> apiClients = new ArrayList<>();

    public ShopSession(String testName) {
        this.name = testName.replaceAll("[^A-Za-z0-9._-]+", "_");
        Browser browser = BrowserManager.browser();
        this.context = browser.newContext(new Browser.NewContextOptions()
                .setBaseURL(Config.baseUrl())
                .setViewportSize(1280, 900));
        context.setDefaultTimeout(Config.timeoutMillis());
        PlaywrightAssertions.setDefaultAssertionTimeout(Config.timeoutMillis());
        context.tracing().start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true).setSources(true));
        this.page = context.newPage();
    }

    public Page page() {
        return page;
    }

    public BrowserContext context() {
        return context;
    }

    /** API calls made through the browser context: logging in here logs the page in too. */
    public ShopApi ui() {
        return new ShopApi(context.request());
    }

    /** A brand-new API client with its own cookie jar, i.e. a separate user. */
    public ShopApi newApiClient() {
        APIRequestContext api = BrowserManager.playwright().request()
                .newContext(new APIRequest.NewContextOptions().setBaseURL(Config.baseUrl()));
        apiClients.add(api);
        return new ShopApi(api);
    }

    /**
     * Ends the session. On failure, saves trace.zip and a full-page screenshot and returns the
     * screenshot bytes (so Cucumber can attach them to its report); otherwise returns null.
     * Open a trace with:  mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="show-trace <path>"
     */
    public byte[] close(boolean failed) {
        byte[] screenshot = null;
        try {
            if (failed) {
                Path dir = Config.artifactsDir().resolve(name);
                Files.createDirectories(dir);
                screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true)
                        .setPath(dir.resolve("screenshot.png")));
                context.tracing().stop(new Tracing.StopOptions().setPath(dir.resolve("trace.zip")));
                System.out.println("Failure artifacts: " + dir.toAbsolutePath());
            } else {
                context.tracing().stop();
            }
        } catch (Exception e) {
            System.out.println("Could not save failure artifacts: " + e.getMessage());
        } finally {
            apiClients.forEach(APIRequestContext::dispose);
            context.close();
        }
        return screenshot;
    }
}
