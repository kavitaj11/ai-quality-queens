# 6. Mobile Web and Framework Design

## The verified mobile-web suite

`MobileWebShopTests` runs the practice shop in a mobile browser. On the machine used for this tutorial it ran through a real Appium 3.9.1 server and the Chromium driver, with Chrome emulating a Pixel 7:

| Test | Checks | Result |
|---|---|---|
| `pageUsesAPhoneSizedViewport` | `window.innerWidth` < 600 (phone layout) | ✅ pass |
| `windowIsPortrait` | height > width | ✅ pass |
| `wrongPasswordShowsError` | error message on a bad login | ✅ pass |
| `shopperCanSearchAndCheckOutOnAPhone` | login → search "lamp" → add 2 Desk Lamps → total $69.98 → checkout | ✅ pass |

The same test class runs on real Chrome for Android with `-Dplatform=android-chrome`: the page objects don't change, only the session does.

## Framework layers

```
tests/              MobileWebShopTests, AndroidSettingsTests
pages/web/          LoginScreen, ProductsScreen, CartScreen       (CSS / data-testid)
pages/nativeapp/    SettingsScreen                                (resource id / UiAutomator)
gestures/           Gestures (W3C Actions)
driver/             DriverFactory: ThreadLocal<AppiumDriver>, options per platform
config/             MobileConfig: platform, appiumUrl, deviceName, shopUrl, timeouts
```

Framework design decisions:

- **One platform switch** (`-Dplatform=android-native|android-chrome|desktop-chromium`) selects the session; tests and page objects stay the same.
- **ThreadLocal driver, never stored in a field** (`BaseMobileTest.driver()` reads it each time), for the same reason as in the Selenium tutorial: test classes are shared between threads in parallel runs.
- **Config knows the network path**: `shopUrl` defaults to `http://10.0.2.2:8091` for Android platforms and `http://127.0.0.1:8091` on the desktop.
- **Screenshots on failure** in `BaseMobileTest`.

## Cross-platform page objects (Android + iOS)

When one app exists on both platforms, keep one page-object interface and platform-specific locators. java-client's `@AndroidFindBy` / `@iOSXCUITFindBy` annotations with `AppiumFieldDecorator` do this:

```java
@AndroidFindBy(accessibility = "login-button")
@iOSXCUITFindBy(accessibility = "login-button")
private WebElement loginButton;

public LoginScreen(AppiumDriver driver) {
    PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
}
```

If both platforms share accessibility ids, a single `AppiumBy.accessibilityId(...)` works on both, which is one more reason to ask for them.

## Running on several devices in parallel

Each parallel Appium session needs its own:

- device: `appium:udid` (from `adb devices`)
- UiAutomator2 port: `appium:systemPort` (for example 8200, 8201, ...)
- chromedriver port for web/hybrid: `appium:chromedriverPort`
- on iOS: `appium:wdaLocalPort`

Pass them per thread (TestNG parameters per `<test>` block, or a device pool), or use a device farm that assigns them.

## Review questions

**How do you design an Appium framework?**
Layers: config, ThreadLocal driver factory with options per platform, page objects (platform-specific locators where needed), gesture utilities, tests. One switch selects the platform; tests don't change.

**How do you share tests between Android and iOS?**
One page-object API with platform-specific locators (`@AndroidFindBy`/`@iOSXCUITFindBy`), or shared accessibility ids so one locator works on both.

**How do you run on several devices in parallel?**
One session per thread with unique `udid`, `systemPort` (Android) or `wdaLocalPort` (iOS), and chromedriver ports. Or a cloud device farm.

**Mobile web testing: emulation or a real device?**
Desktop emulation (as verified here) is fast and catches layout and flow problems. Real devices catch real-browser, performance and touch issues. Use both: emulation in every build, real devices before release.

Next: [7. CI, device farms and AI](07-ci-device-farms-and-ai.md)
