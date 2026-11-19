# 3. Capabilities and Sessions

**Capabilities** are the settings sent when a session starts: which platform, which driver, which device, which app. In java-client, **options classes** build them with typed methods.

## The three sessions in `DriverFactory`

**Native app: Android's Settings app** (present on every emulator, so no APK is needed):

```java
new UiAutomator2Options()
        .setDeviceName(MobileConfig.deviceName())    // as shown by `adb devices`
        .setAppPackage("com.android.settings")
        .setAppActivity(".Settings")
        .setNoReset(true)
        .setNewCommandTimeout(Duration.ofSeconds(120));
```

**Mobile web: Chrome on Android:**

```java
UiAutomator2Options options = new UiAutomator2Options()
        .setDeviceName(MobileConfig.deviceName())
        .withBrowserName("Chrome");
options.setCapability("appium:chromedriverAutodownload", true);
```

**Desktop Chrome through Appium's Chromium driver** (how this tutorial was verified without an emulator):

```java
ChromiumOptions options = new ChromiumOptions().withBrowserName("chrome");
options.setPlatformName(platformName);    // windows, mac or linux
options.setCapability("goog:chromeOptions", Map.of(
        "args", List.of("--headless=new", "--window-size=412,915"),
        "mobileEmulation", Map.of("deviceName", "Pixel 7")));
```

### A real error found while writing this tutorial

The first desktop-Chromium session failed:

```
SessionNotCreated ... Response code 400. Message: 'platformName' is required to be present
```

Plain Selenium doesn't need `platformName`; **Appium does, for every driver**. The fix was one line (`setPlatformName`). Read the server's error message first: Appium is very precise about what's missing.

## Capabilities you'll be asked about

| Capability | Meaning |
|---|---|
| `platformName` | `Android`, `iOS`, `Windows`, ... (required) |
| `appium:automationName` | Which driver: `UiAutomator2`, `XCUITest`, `Espresso`, `Chromium` ... |
| `appium:deviceName` / `appium:udid` | Which device; `udid` is exact and needed with several devices |
| `appium:app` | Path or URL of an APK/IPA to install |
| `appium:appPackage` / `appium:appActivity` | Launch an installed Android app |
| `appium:bundleId` | Launch an installed iOS app |
| `browserName` | `Chrome` / `Safari` for mobile web |
| `appium:noReset` | Keep app data between sessions (faster; tests must handle existing state) |
| `appium:fullReset` | Uninstall and reinstall the app (clean but slow) |
| `appium:newCommandTimeout` | Seconds without a command before the server ends the session |
| `appium:autoGrantPermissions` | Grant Android runtime permissions automatically |
| `appium:systemPort` / `appium:wdaLocalPort` | Unique ports per parallel session (Android / iOS) |

Appium-specific capabilities carry the **`appium:` prefix** (the options classes add it for you). Standard W3C ones (`platformName`, `browserName`) don't.

## Reset strategies

| Strategy | Effect | Use when |
|---|---|---|
| Default (no flags) | App restarted, data kept between tests depends on driver | Most suites |
| `noReset=true` | Don't clear data or reinstall | Fast local runs; app state handled by tests |
| `fullReset=true` | Uninstall and reinstall | Clean-install scenarios, CI where isolation matters |

## Review questions

**What are desired capabilities?**
Key-value settings sent when creating a session: platform, driver, device, app and behaviour (reset, timeouts, permissions). java-client's options classes (`UiAutomator2Options`, `XCUITestOptions`) build them type-safely.

**app vs appPackage/appActivity?**
`app` installs a build from a file or URL; `appPackage` + `appActivity` launch an app that's already installed.

**noReset vs fullReset?**
`noReset` keeps app data and skips reinstalling (fast, but tests must cope with leftover state); `fullReset` reinstalls (clean, slow).

**Why do some capabilities have an `appium:` prefix?**
W3C reserves un-prefixed names for standard capabilities; vendor-specific ones must be namespaced.

Next: [4. Locators for native apps](04-locators-for-native-apps.md)
