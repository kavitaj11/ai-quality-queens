# 5. Waits, Gestures and Device Actions

## Waits

Mobile is slower and less predictable than desktop: app launch, animations, network. The rules are the same as in Selenium:

- Use `WebDriverWait` with explicit conditions; no `Thread.sleep`.
- Avoid a global implicit wait; it slows every "element should be absent" check.
- After an action that changes screens, wait for something only the **new** screen has. (The mobile-web screens reuse the `stalenessOf` technique from the Selenium tutorial: `tapAndWaitForNewPage`.)

## Gestures with W3C Actions

`TouchAction` and `MultiTouchAction` were **removed in Appium 2**. Gestures are W3C Actions sequences: a "finger" pointer that moves, presses, pauses and releases. From `gestures/Gestures.java`:

```java
PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
Sequence swipe = new Sequence(finger, 0)
        .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), from.x, from.y))
        .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
        .addAction(finger.createPointerMove(duration, PointerInput.Origin.viewport(), to.x, to.y))
        .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
((RemoteWebDriver) driver).perform(List.of(swipe));
```

A **long press** is the same sequence with a pause (a move to the same point over the hold time) between down and up. **Pinch/zoom** uses two fingers: two sequences performed together.

Drivers also offer **mobile commands** for common gestures, which can be more reliable than raw actions:

```java
driver.executeScript("mobile: swipeGesture", Map.of(
        "left", 100, "top", 500, "width", 600, "height", 800, "direction", "up", "percent", 0.75));
```

(`mobile: scrollGesture`, `mobile: longClickGesture`, `mobile: pinchOpenGesture` and others exist for UiAutomator2; XCUITest has its own set.)

## Device and app actions

From `AndroidSettingsTests` (compiled; runs on an emulator):

```java
android().pressKey(new KeyEvent(AndroidKey.HOME));         // hardware key
android().activateApp("com.android.settings");              // bring an app to the foreground
android().currentActivity();                                // which screen is showing
```

| Action | Java |
|---|---|
| Background / foreground app | `terminateApp(id)`, `activateApp(id)`, `runAppInBackground(Duration)` |
| Install / remove app | `installApp(path)`, `removeApp(id)`, `isAppInstalled(id)` |
| Keyboard | `hideKeyboard()`, `isKeyboardShown()` |
| Orientation | `rotate(ScreenOrientation.LANDSCAPE)` |
| Network (Android emulator) | `setConnection(...)`, or `mobile: shell` with adb commands |
| Notifications | `openNotifications()` |
| Screenshots / screen recording | `getScreenshotAs(...)`, `startRecordingScreen()` / `stopRecordingScreen()` |

## Hybrid apps: contexts

A hybrid app shows web content inside a native WebView. Switch context to use web locators:

```java
Set<String> contexts = ((SupportsContextSwitching) driver).getContextHandles();  // [NATIVE_APP, WEBVIEW_com.example]
((SupportsContextSwitching) driver).context("WEBVIEW_com.example");             // now CSS/XPath on the web content
((SupportsContextSwitching) driver).context("NATIVE_APP");                      // back to native
```

The app must enable WebView debugging in its debug build for Appium to see the WebView.

## Review questions

**How do you swipe or scroll in Appium 2+?**
W3C Actions with a touch `PointerInput` (move, down, move over a duration, up), or driver `mobile:` gesture commands. On Android, `UiScrollable` for scrolling to an element.

**What replaced TouchAction?**
W3C Actions (`PointerInput` + `Sequence`). TouchAction and MultiTouchAction were removed in Appium 2.

**How do you test a hybrid app?**
Use `getContextHandles()` and switch to the `WEBVIEW_*` context for web content, back to `NATIVE_APP` for native parts.

**How do you test app behaviour when it goes to the background?**
`runAppInBackground(Duration)` or `terminateApp`/`activateApp`, then assert state is restored.

Next: [6. Mobile web and framework design](06-mobile-web-and-framework-design.md)
