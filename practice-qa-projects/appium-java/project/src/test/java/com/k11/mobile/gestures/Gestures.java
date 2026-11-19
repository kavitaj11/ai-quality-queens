package com.k11.mobile.gestures;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Touch gestures with W3C Actions, the standard way since Appium 2 (TouchAction and
 * MultiTouchAction were removed). A gesture is a sequence of pointer moves, presses,
 * pauses and releases on a "finger".
 */
public final class Gestures {

    private Gestures() {
    }

    /** Swipe up across the middle of the screen, for example to scroll a list. */
    public static void swipeUp(WebDriver driver) {
        Dimension size = driver.manage().window().getSize();
        int x = size.width / 2;
        swipe(driver, new Point(x, (int) (size.height * 0.75)), new Point(x, (int) (size.height * 0.25)),
                Duration.ofMillis(600));
    }

    public static void swipe(WebDriver driver, Point from, Point to, Duration duration) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 0)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), from.x, from.y))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger.createPointerMove(duration, PointerInput.Origin.viewport(), to.x, to.y))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        ((RemoteWebDriver) driver).perform(List.of(swipe));
    }

    /** Long-press an element, for example to open a context menu. */
    public static void longPress(WebDriver driver, WebElement element, Duration hold) {
        Point centre = new Point(element.getRect().x + element.getRect().width / 2,
                element.getRect().y + element.getRect().height / 2);
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence press = new Sequence(finger, 0)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centre.x, centre.y))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger.createPointerMove(hold, PointerInput.Origin.viewport(), centre.x, centre.y))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        ((RemoteWebDriver) driver).perform(List.of(press));
    }
}
