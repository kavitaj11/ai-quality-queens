# 2. Environment Setup

## Path A: no emulator (what this tutorial was verified with)

You need Java 17+, Maven, Node.js 20.19+ and Chrome.

```bash
npm install -g appium                    # Appium 3
appium driver install chromium           # check the driver version matches your Appium major version
appium --allow-insecure "chromium:chromedriver_autodownload"
```

> **Version compatibility is real.** When this tutorial was written, the newest Chromium driver required the Appium 4 beta, and installing it on Appium 2 failed with *"cannot be installed because the server version it requires (^4.0.0-beta.8) does not meet the currently installed one"*. Pin a compatible driver: `appium driver install --source=npm appium-chromium-driver@2.2.5` works with Appium 3.

In a second terminal:

```bash
python ../practice-apps/shop-web/shop_web.py
cd project && mvn test                     # mobile-web tests, desktop Chrome in Pixel 7 emulation
```

The server log shows Appium finding no chromedriver, downloading one that matches your Chrome (`Chromedriver version: 132.0.6834.159` on the machine used here), and creating one session per test.

## Path B: Android emulator (native and Chrome on Android)

1. **Install Android Studio**, then in *SDK Manager* install an SDK platform, *Android SDK Platform-Tools* and *Android Emulator*.
2. **Set environment variables** (Appium needs them):

```bash
export ANDROID_HOME=$HOME/Android/Sdk                      # Windows: %LOCALAPPDATA%\Android\Sdk
export PATH=$PATH:$ANDROID_HOME/platform-tools:$ANDROID_HOME/emulator
```

3. **Create and start an emulator** (Device Manager in Android Studio, or `emulator -avd <name>`), then check it's visible:

```bash
adb devices          # emulator-5554   device
```

4. **Install the driver and check the setup:**

```bash
appium driver install uiautomator2
appium driver doctor uiautomator2      # checks ANDROID_HOME, Java, adb and more
appium --allow-insecure "uiautomator2:chromedriver_autodownload"
```

5. **Start the shop so the emulator can reach it.** The emulator sees your computer as `10.0.2.2`:

```bash
python ../practice-apps/shop-web/shop_web.py --host 0.0.0.0
```

6. **Run:**

```bash
mvn test -Dplatform=android-chrome -DdeviceName=emulator-5554           # mobile web in Chrome on Android
mvn test -Dsuite=testng-android-native.xml -Dplatform=android-native    # native Settings app
```

## Appium Inspector

**Appium Inspector** (a separate desktop app) connects to your Appium server, starts a session with your capabilities, and shows the app's element tree with every attribute and suggested locators. It's the native-app equivalent of browser dev tools, and the fastest way to find locators.

## iOS in one paragraph

iOS automation needs a Mac with Xcode, the `xcuitest` driver (`appium driver install xcuitest`), and WebDriverAgent signed for real devices (simulators need no signing). The Java structure is identical: `XCUITestOptions` and `IOSDriver` instead of `UiAutomator2Options` and `AndroidDriver`.

## Review questions

**What do you need to run Appium tests on Android?**
JDK, Android SDK (platform-tools, emulator), `ANDROID_HOME` set, an emulator or device visible in `adb devices`, the Appium server and the `uiautomator2` driver.

**How does an emulator reach a server on your computer?**
Through `10.0.2.2`, the emulator's alias for the host. `127.0.0.1` inside the emulator is the emulator itself.

**How do you find locators in a native app?**
Appium Inspector (or Android Studio's Layout Inspector), looking for accessibility ids first, then resource ids.

**How do you check your setup?**
`appium driver doctor uiautomator2` (Appium 2+), plus `adb devices`.

Next: [3. Capabilities and sessions](03-capabilities-and-sessions.md)
