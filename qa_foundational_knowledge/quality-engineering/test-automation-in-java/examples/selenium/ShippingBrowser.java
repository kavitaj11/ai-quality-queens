//DEPS org.seleniumhq.selenium:selenium-java:4.27.0
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ShippingBrowser {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver(new ChromeOptions().addArguments("--headless=new"));
        try {
            driver.get("http://127.0.0.1:8000");
            driver.findElement(By.id("subtotal")).sendKeys("5000");
            driver.findElement(By.id("calculate")).click();
            new WebDriverWait(driver, Duration.ofSeconds(5)).until(
                ExpectedConditions.textToBe(By.cssSelector("[role=status]"), "Free shipping"));
        } finally {
            driver.quit();
        }
    }
}
