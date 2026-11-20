# OOP: encapsulation, inheritance, polymorphism

[03 · Module topics](../../modules/03-language-deep-dives.md) · [Curriculum index](../../README.md)

**Category** · JAVA DEEP DIVE

Object-oriented design is the backbone of the Page Object Model. Encapsulation hides state behind methods (private fields, public actions); inheritance shares behavior (a BasePage every page extends); polymorphism lets one reference drive many concrete types. Master these and POM writes itself.

## Worked reference

````text
// Encapsulation: state is private; behavior is public
class LoginPage {
    private final WebDriver driver;      // hidden
    LoginPage(WebDriver d) { this.driver = d; }
    void login(String u, String p) {     // exposed action
        driver.findElement(By.id("email")).sendKeys(u);
        driver.findElement(By.id("submit")).click();
    }
}

// Inheritance: shared behavior in a base class
abstract class BasePage {
    protected final WebDriver driver;
    BasePage(WebDriver d) { this.driver = d; }
    abstract boolean isLoaded();          // each page defines
    void waitUntilLoaded() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(d -> isLoaded());
    }
}

// Polymorphism: one reference type, many concrete pages
BasePage page = new DashboardPage(driver);
page.waitUntilLoaded();                   // calls the right isLoaded()
````

## Exercise

Design a BasePage with a shared waitUntilLoaded(), then two pages that override isLoaded() differently — and drive both through a BasePage reference.
