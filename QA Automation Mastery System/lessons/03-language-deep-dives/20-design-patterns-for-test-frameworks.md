# Design patterns for test frameworks

[03 · Module topics](../../modules/03-language-deep-dives.md) · [Curriculum index](../../README.md)

**Category** · JAVA DEEP DIVE

The patterns that keep an SDET framework maintainable, and that interviewers love to probe. Page Object Model (the base pattern), Singleton (one WebDriver/config), Factory (create the right driver/page), Builder (fluent test-data + RequestSpec), and Fluent Interface (chained page methods that read like a script). Each solves a real framework problem — know the intent, not just the diagram.

## Worked reference

````text
// ── FACTORY — create the right object from a key ──
class DriverFactory {
    static WebDriver create(String browser) {
        return switch (browser) {
            case "chrome"  -> new ChromeDriver();
            case "firefox" -> new FirefoxDriver();
            default -> throw new IllegalArgumentException(browser);
        };
    }
}

// ── SINGLETON — one thread-safe driver per thread ──
class DriverManager {
    private static final ThreadLocal<WebDriver> DRIVER =
        new ThreadLocal<>();
    static WebDriver get() {
        if (DRIVER.get() == null)
            DRIVER.set(DriverFactory.create(
                System.getProperty("browser", "chrome")));
        return DRIVER.get();
    }
    static void quit() {
        if (DRIVER.get() != null) {
            DRIVER.get().quit(); DRIVER.remove();
        }
    }
}

// ── BUILDER — fluent, readable test data (defaults + overrides) ──
class UserBuilder {
    private String email = "qa+" + UUID.randomUUID() + "@test.dev";
    private String role  = "customer";
    UserBuilder role(String r)  { this.role = r;  return this; }
    UserBuilder email(String e) { this.email = e; return this; }
    User build() { return new User(email, role); }
}
User admin = new UserBuilder().role("admin").build();

// ── PAGE OBJECT + FLUENT INTERFACE — chain like a script ──
class LoginPage {
    private final WebDriver driver;
    LoginPage(WebDriver d) { this.driver = d; }
    LoginPage typeEmail(String e) {
        driver.findElement(By.id("email")).sendKeys(e);
        return this;                       // return this -> chainable
    }
    LoginPage typePassword(String p) {
        driver.findElement(By.id("password")).sendKeys(p);
        return this;
    }
    DashboardPage submit() {               // navigation returns next page
        driver.findElement(By.id("go")).click();
        return new DashboardPage(driver);
    }
}

// Reads like the user's journey:
new LoginPage(DriverManager.get())
    .typeEmail(admin.email())
    .typePassword("s3cret")
    .submit()
    .assertLoaded();
````

## Exercise

Refactor a framework: add a DriverFactory (Factory), a thread-safe DriverManager (Singleton), and a UserBuilder (Builder). Chain two page methods with a Fluent Interface.
