# 1. Architecture and Setup

## How Playwright for Java works

```
Java test ──► Playwright Java library ──(one persistent connection)──► Playwright driver (Node.js, bundled) ──► Chromium / Firefox / WebKit
```

The Java library ships with the Playwright driver inside its jar (`driver-bundle`). On first use it downloads the browser builds it was tested with into a shared cache, so there is nothing else to install.

| Object | What it is | Lifetime in this framework |
|---|---|---|
| `Playwright` | The connection to the driver. **Not thread-safe.** | One per thread (`BrowserManager`) |
| `Browser` | A browser process; slow to start | One per thread, reused |
| `BrowserContext` | An isolated session (cookies, storage), like a fresh incognito window; cheap | **One per test** (`ShopSession`) |
| `Page` | A tab | One per test |
| `APIRequestContext` | An HTTP client for API calls | Browser-shared (`context.request()`) or standalone |

From `support/BrowserManager.java`:

```java
private static final ThreadLocal<Playwright> PLAYWRIGHT = new ThreadLocal<>();
private static final ThreadLocal<Browser> BROWSER = new ThreadLocal<>();

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
```

And from `support/ShopSession.java`, a fresh context per test with tracing on:

```java
this.context = browser.newContext(new Browser.NewContextOptions()
        .setBaseURL(Config.baseUrl())
        .setViewportSize(1280, 900));
context.tracing().start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true).setSources(true));
this.page = context.newPage();
```

`setBaseURL` lets tests use relative paths: `page.navigate("/cart")`, `api.get("/api/orders/...")`.

## Playwright vs Selenium (technical comparison)

| | Playwright for Java | Selenium WebDriver |
|---|---|---|
| Waiting | Auto-waits before actions; assertions retry | Explicit `WebDriverWait` |
| Isolation | Cheap `BrowserContext` per test | Usually a new browser per test |
| API testing | Built in: `APIRequestContext` | Separate library (REST Assured) |
| Network | Intercept and mock with `page.route()` | Limited (CDP, Chromium only) |
| Debugging | Trace viewer: DOM, network, console per step | Screenshots and logs |
| Browsers | Chromium, Firefox, WebKit (bundled builds) | Any W3C driver, including real Safari |
| Ecosystem | Newer | Very large; Grid; every cloud provider |

## Dependencies (from `pom.xml`)

| Library | Why |
|---|---|
| `com.microsoft.playwright:playwright` | UI and API automation |
| `cucumber-java`, `cucumber-picocontainer`, `cucumber-junit-platform-engine` | BDD, dependency injection, running on JUnit 5 |
| `junit-jupiter`, `junit-platform-suite` | JUnit 5 tests, parameterized tests, the `@Suite` runner |
| `jackson-databind`, `jackson-datatype-jsr310` | JSON parsing into records, dates |
| `com.networknt:json-schema-validator` | JSON Schema validation |
| `org.xerial:sqlite-jdbc` | Database checks against the app's SQLite file |
| `assertj-core` | Readable value assertions |

## Configuration

`config/Config.java` reads each setting from a `-D` property, then an environment variable, then a default:

| Property | Env var | Default |
|---|---|---|
| `baseUrl` | `BASE_URL` | `http://127.0.0.1:8091` |
| `browser` | `BROWSER` | `chromium` (`firefox`, `webkit`) |
| `headless` | `HEADLESS` | `true` |
| `slowMo` | `SLOW_MO` | `0` ms |
| `timeoutMillis` | `TIMEOUT_MILLIS` | `10000` |
| `shopPassword` | `SHOP_PASSWORD` | the practice shop's test password |
| `dbPath` | `SHOP_DB_PATH` | `../../practice-apps/shop-web/shop.db` |

## Review questions

**Why one Playwright instance per thread?**
`Playwright` objects aren't thread-safe. Parallel tests need one per thread; each test still gets its own `BrowserContext`.

**Why a new BrowserContext per test instead of a new browser?**
A context gives full isolation (cookies, storage, cache) in milliseconds; launching a browser takes seconds.

**How are browsers installed for Playwright Java?**
The library downloads the matching builds on first use (or run the CLI's `install` command in CI images). In CI, Microsoft's Playwright Java Docker images already contain them.

Next: [2. Locators, waiting and assertions](02-locators-waiting-and-assertions.md)
