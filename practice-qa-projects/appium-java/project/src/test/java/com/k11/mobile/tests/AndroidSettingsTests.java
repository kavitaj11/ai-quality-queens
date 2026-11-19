package com.k11.mobile.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.k11.mobile.gestures.Gestures;
import com.k11.mobile.pages.nativeapp.SettingsScreen;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.testng.annotations.Test;

/**
 * Native Android tests against the built-in Settings app, so no APK is needed.
 * Needs an Android emulator or device and the UiAutomator2 driver:
 *   mvn test -Dsuite=testng-android-native.xml -Dplatform=android-native -DdeviceName=emulator-5554
 * Row names differ slightly between Android versions; adjust them to your emulator.
 */
public class AndroidSettingsTests extends BaseMobileTest {

    private AndroidDriver android() {
        return (AndroidDriver) driver();
    }

    @Test(groups = "android-native")
    public void settingsAppOpensOnItsMainScreen() {
        assertThat(android().currentActivity()).contains("Settings");
        new SettingsScreen(android()).waitUntilLoaded();
    }

    @Test(groups = "android-native")
    public void canScrollToAndOpenAboutPhone() {
        SettingsScreen settings = new SettingsScreen(android()).waitUntilLoaded();

        settings.openRow("About emulated device");   // "About phone" on a real phone
        assertThat(settings.isShowingText("Android version")).isTrue();

        settings.goBack();
        settings.waitUntilLoaded();
    }

    @Test(groups = "android-native")
    public void swipeAndHardwareKeysWork() {
        SettingsScreen settings = new SettingsScreen(android()).waitUntilLoaded();

        Gestures.swipeUp(android());
        android().pressKey(new KeyEvent(AndroidKey.HOME));
        assertThat(android().getCurrentPackage()).isNotEqualTo("com.android.settings");

        android().activateApp("com.android.settings");
        settings.waitUntilLoaded();
    }
}
