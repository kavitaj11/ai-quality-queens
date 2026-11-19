# Appium with Java: Hands-On Tutorial

Verification figures below describe the reference lab runs. For checks performed on this copy and remaining environment requirements, see [validation notes](../VALIDATION.md).

**Level:** beginner to intermediate · **Time:** about 5–6 hours (plus Android setup) · **Last reviewed:** November 2026 (Appium 3.9, java-client 10.0, Selenium 4.45, TestNG 7.12)

Learn mobile test automation with Appium 3 and Java: native Android apps, mobile web, gestures, framework design and CI. Each chapter ends with the **review questions** and practical exercises.

## What was verified, and what wasn't

| Part | Status |
|---|---|
| Whole project compiles (native Android, Chrome on Android, desktop Chromium) | ✅ verified with Maven |
| **Mobile-web tests through a real Appium 3.9.1 server** (Chromium driver, desktop Chrome in Pixel 7 emulation) | ✅ **4 tests pass**; Appium auto-downloaded the matching chromedriver |
| Native Android tests (Settings app) on an emulator | ⚠️ compiled, **not run**: no Android SDK on the machine this was written on |
| Mobile web on Chrome for Android | ⚠️ compiled, **not run**, same reason |

If you have an Android emulator, Chapter 2 shows how to run the rest. Row names in the Settings tests can differ between Android versions.

## Chapters

| # | Chapter | Technical topics |
|---|---|---|
| 1 | [Appium architecture](01-appium-architecture.md) | Server, drivers, plugins, W3C, Appium 2 → 3 changes |
| 2 | [Environment setup](02-environment-setup.md) | Android SDK, emulator, drivers, Appium Inspector, doctor |
| 3 | [Capabilities and sessions](03-capabilities-and-sessions.md) | Options classes, native vs web vs hybrid, reset strategies |
| 4 | [Locators for native apps](04-locators-for-native-apps.md) | Accessibility id, resource id, UiAutomator, why not XPath |
| 5 | [Waits, gestures and device actions](05-waits-gestures-and-device-actions.md) | W3C Actions, scrolling, keys, app lifecycle, contexts |
| 6 | [Mobile web and framework design](06-mobile-web-and-framework-design.md) | Page objects across platforms, parallel devices |
| 7 | [CI, device farms and AI](07-ci-device-farms-and-ai.md) | Emulators in CI, real devices, self-healing locators |
| 8 | [Review questions and exercises](08-review-questions-and-exercises.md) | Rapid-fire Q&A, debugging scenarios, tasks |

## Quick start (no emulator needed)

```bash
# 1. The practice shop
python ../practice-apps/shop-web/shop_web.py

# 2. Appium 3 and the Chromium driver (Node.js 20.19+)
npm install -g appium
appium driver install chromium
appium --allow-insecure "chromium:chromedriver_autodownload"

# 3. The tests: mobile web through Appium, in desktop Chrome with phone emulation
cd project
mvn test
```

## Project layout

```
project/
├── pom.xml                           java-client 10, Selenium 4.45, TestNG, AssertJ
├── testng-mobile-web.xml / testng-android-native.xml
└── src/test/java/com/k11/mobile/
    ├── config/MobileConfig.java      platform, Appium URL, device, shop URL (10.0.2.2 on emulators)
    ├── driver/DriverFactory.java     one ThreadLocal session; options for each platform
    ├── pages/web/                    mobile-web screens (login, products, cart)
    ├── pages/nativeapp/SettingsScreen.java   native Android screen
    ├── gestures/Gestures.java        swipe and long-press with W3C Actions
    └── tests/                        MobileWebShopTests, AndroidSettingsTests
```

Next: [1. Appium architecture](01-appium-architecture.md)
