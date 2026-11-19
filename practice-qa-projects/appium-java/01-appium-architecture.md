# 1. Appium Architecture

```
Java test (java-client) ──W3C WebDriver over HTTP──► Appium server (Node.js) ──► driver ──► device / app
                                                     │                         UiAutomator2 ──► Android (via UiAutomator2 server on the device)
                                                     │                         XCUITest    ──► iOS (via WebDriverAgent)
                                                     │                         Espresso, Flutter, Mac2, Windows, Chromium, Gecko ...
                                                     └── plugins (images, gestures, device farm, ...)
```

- **Appium server**: a Node.js HTTP server that speaks the W3C WebDriver protocol, the same protocol Selenium uses. That's why Appium tests look like Selenium tests.
- **Drivers**: installed separately since Appium 2. Each driver automates one platform: `uiautomator2` (Android), `xcuitest` (iOS), `espresso`, `flutter`, `mac2`, `windows`, and `chromium` for desktop Chrome (used to verify this tutorial).
- **Plugins**: optional server extensions, for example image-based element finding or device-farm management.
- **Client libraries**: java-client for Java (built on Selenium's Java bindings), plus Python, JavaScript (WebdriverIO), Ruby and .NET.
- **The `automationName` capability** tells the server which driver to use for the session.

## What changed: Appium 1 → 2 → 3

| Version | Key change |
|---|---|
| **Appium 2** | Drivers and plugins became separate installs (`appium driver install uiautomator2`); W3C protocol only; `appium:` prefix for Appium capabilities; `TouchAction`/`MultiTouchAction` removed in favour of W3C Actions |
| **Appium 3** (current) | Requires a newer Node.js (20.19+); older deprecated endpoints removed; **insecure features must name the driver**, e.g. `--allow-insecure chromium:chromedriver_autodownload` |
| Appium 4 | In beta at the time of writing. The newest Chromium driver already requires it, so this tutorial pinned Chromium driver 2.2.5 for Appium 3 |

Key architecture changes: "Drivers are installed separately, W3C only, and the old TouchAction API is gone" covers Appium 2. "Stricter, namespaced security flags and a newer Node.js baseline" covers Appium 3.

## Native, mobile web and hybrid apps

| App type | Example | How Appium automates it |
|---|---|---|
| **Native** | Settings, a banking app | Platform driver (UiAutomator2/XCUITest); native locators |
| **Mobile web** | The practice shop in Chrome on a phone | `browserName=Chrome` (Android) or `Safari` (iOS); web locators, via chromedriver/Safari |
| **Hybrid** | A native shell with WebViews inside | Switch context between `NATIVE_APP` and `WEBVIEW_...` |

## Review questions

**Explain Appium's architecture.**
A Node.js server speaking W3C WebDriver. The client sends commands; the `automationName` capability picks a driver (UiAutomator2, XCUITest...), which controls the device through the platform's own automation framework.

**Why is Appium's API so similar to Selenium's?**
Both implement the W3C WebDriver protocol, and the Java client is built on Selenium's Java bindings: `findElement`, `click`, `WebDriverWait` all work the same.

**What changed in Appium 2?**
Drivers and plugins are installed separately, only the W3C protocol is supported, Appium-specific capabilities need the `appium:` prefix, and touch gestures use W3C Actions instead of TouchAction.

**Do you need the app's source code to test it with Appium?**
No. Appium tests the compiled app (APK/IPA) from the outside, like a user. That's a key difference from Espresso or XCUITest written inside the app's project.

Next: [2. Environment setup](02-environment-setup.md)
