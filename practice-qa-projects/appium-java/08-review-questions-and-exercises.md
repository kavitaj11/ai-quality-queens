# 8. Review Questions and Exercises

## Rapid-fire questions

| Question | Short answer |
|---|---|
| What is Appium? | An open-source, W3C WebDriver server for automating native, mobile web and hybrid apps on Android, iOS and more, without changing the app. |
| Appium vs Espresso/XCUITest? | Appium tests the built app from outside in any language, cross-platform. Espresso/XCUITest live in the app's code, are faster and more stable, but are platform- and language-specific. |
| Which driver for Android? iOS? | UiAutomator2 (or Espresso); XCUITest. |
| Default Appium port? | 4723. |
| How do you get the current Android screen? | `currentActivity()` (and `getCurrentPackage()`). |
| How do you hide the keyboard? | `hideKeyboard()`. |
| How do you handle permission pop-ups? | `appium:autoGrantPermissions=true`, or tap the system dialog's buttons by resource id. |
| How do you test deep links? | `driver.executeScript("mobile: deepLink", Map.of("url", "...", "package", "..."))` on Android. |
| How do you take a screen recording? | `startRecordingScreen()` / `stopRecordingScreen()` (returns Base64 video). |
| What is UDID? | The unique id of a device; needed to choose a device when several are connected. |
| What is WebDriverAgent? | The iOS server app that XCUITest driver installs on the device to execute commands. |
| What is `newCommandTimeout`? | How long the server waits without a command before ending the session. |

## Debugging scenarios

1. **"What breaks when you upgrade Appium?"** Driver/server compatibility. When writing this tutorial, the newest Chromium driver needed Appium 4 beta; the fix was to pin a driver version that matches the server (Chapter 2).
2. **"Why might an Appium session fail to start?"** `'platformName' is required to be present`: Appium needs `platformName` even where Selenium doesn't (Chapter 3).
3. **"Why doesn't the emulator reach your local server?"** Inside the emulator, `127.0.0.1` is the emulator; the host is `10.0.2.2` (Chapter 2).
4. **"How do you scroll to an element in a long list?"** `UiScrollable.scrollIntoView` on Android, W3C Actions swipes otherwise (Chapters 4–5).
5. **"How do you run tests on several devices at once?"** Unique `udid`, `systemPort`, chromedriver and WDA ports per session, or a device farm (Chapter 6).

## Exercises

1. **Run the verified path.** Start the shop and Appium with the Chromium driver, then `mvn test`. Read the Appium server log for one test: find the session creation and the chromedriver download.
2. **Change the device.** Switch the emulated device in `DriverFactory` to `"iPhone 12 Pro"` and check the viewport test still passes.
3. **Watch it.** Run with `-Dheadless=false`.
4. **Negative control.** Change the expected cart total in `shopperCanSearchAndCheckOutOnAPhone`, run, and check the failure screenshot in `target/screenshots`.
5. **Android setup.** Install Android Studio, create an emulator, install `uiautomator2`, run `appium driver doctor uiautomator2` until clean.
6. **Native tests.** Run `testng-android-native.xml`. Fix the row names in `AndroidSettingsTests` for your Android version using Appium Inspector.
7. **Hybrid.** Find a sample hybrid app, list its contexts and switch into the WebView.
8. **Gesture.** Add a pinch-open gesture to `Gestures` using two `PointerInput` fingers.
9. **Parallel.** Start two emulators and run the native suite on both at once with different `udid` and `systemPort` values.

---

Back to the [Appium tutorial index](README.md) · [All tutorials](../README.md)
