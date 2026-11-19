//DEPS com.microsoft.playwright:playwright:1.52.0
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ShippingBrowser {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch();
            try (BrowserContext session = browser.newContext()) {
                Page page = session.newPage();
                page.navigate("http://127.0.0.1:8000");
                page.getByLabel("Subtotal (cents)").fill("4999");
                page.getByRole(AriaRole.BUTTON).click();
                assertThat(page.getByRole(AriaRole.STATUS)).hasText("Shipping: 499 cents");
            }
        }
    }
}
