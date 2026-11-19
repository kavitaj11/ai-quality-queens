# 4. Locators for Native Apps

Native apps have no HTML or CSS. Elements are platform views (Android `TextView`, `Button`, iOS `XCUIElementTypeButton`) with their own attributes.

## Preference order

| Rank | Strategy | Java | Notes |
|---|---|---|---|
| 1 | **Accessibility id** | `AppiumBy.accessibilityId("Log in")` | Android `content-desc`, iOS `accessibilityIdentifier`. Cross-platform, fast; also helps accessibility |
| 2 | **Resource id** (Android) | `AppiumBy.id("com.example:id/login")` | Stable when developers set it |
| 3 | **UiAutomator** (Android) | `AppiumBy.androidUIAutomator("new UiSelector().text(\"Wi-Fi\")")` | Runs on the device; can scroll |
| 3 | **Predicate / class chain** (iOS) | `AppiumBy.iOSNsPredicateString("label == 'Log in'")` | Fast native queries on iOS |
| 4 | Class name | `AppiumBy.className("android.widget.Button")` | Rarely unique |
| ✗ | XPath | `AppiumBy.xpath("//android.widget.TextView[@text='Wi-Fi']")` | Slowest; the whole tree is serialized to XML on every lookup |

Ask the app developers to set **accessibility ids** (Android `contentDescription`, iOS `accessibilityIdentifier`) on the elements tests need, the mobile equivalent of `data-testid`.

## In this project

`SettingsScreen` uses a resource id and UiAutomator selectors:

```java
private static final By ROW_TITLE = AppiumBy.id("android:id/title");

public WebElement scrollToRow(String text) {
    return driver.findElement(AppiumBy.androidUIAutomator(
            "new UiScrollable(new UiSelector().scrollable(true))"
                    + ".scrollIntoView(new UiSelector().text(\"" + text + "\"))"));
}
```

`UiScrollable` runs **on the device**: it scrolls the list until the row appears, which is faster and more reliable than swiping from the test and checking after each swipe.

## Why XPath is slow on mobile

To evaluate XPath, the driver serialises the entire screen's view hierarchy to XML, sends it across, then searches it, on **every** lookup. On complex screens that takes seconds, and on iOS it's especially slow. Native strategies query the platform directly.

## Mobile web locators

In mobile web (Chrome or Safari on a phone) you're automating a web page, so CSS selectors and `data-testid` work exactly as in Selenium. The mobile-web screens in this project use the same `[data-testid='...']` helper as the Selenium tutorial.

## Review questions

**What locator strategy do you use for native apps?**
Accessibility id first (cross-platform, fast, accessibility-friendly), then resource id on Android or predicate/class chain on iOS, then UiAutomator. XPath only as a last resort.

**Why avoid XPath in Appium?**
The whole UI hierarchy is serialised to XML and searched on every lookup, which is slow (especially on iOS) and brittle to layout changes.

**How do you scroll to an element on Android?**
`AppiumBy.androidUIAutomator` with `UiScrollable(...).scrollIntoView(...)`, or W3C Actions swipes in a loop for custom views.

**The app has no accessibility ids. What do you do?**
Ask developers to add them (it helps real users too), and meanwhile use resource ids or UiAutomator/predicate locators scoped to stable containers.

Next: [5. Waits, gestures and device actions](05-waits-gestures-and-device-actions.md)
