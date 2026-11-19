# 1. Selenium Architecture and Setup

## How Selenium WebDriver works

```
Your Java test ──(W3C WebDriver protocol: HTTP + JSON)──► browser driver ──► real browser
   new ChromeDriver()                                       chromedriver        Chrome
   driver.findElement(...)    POST /session/{id}/element    msedgedriver        Edge
   element.click()            POST /session/{id}/element/{id}/click   geckodriver  Firefox
```

- Your code calls the **Selenium client library** (selenium-java).
- The library sends **W3C WebDriver** commands over HTTP to a **driver** executable (chromedriver, msedgedriver, geckodriver).
- The driver controls the browser through the browser's own automation interface.
- With **Selenium Grid**, the same commands go to a Grid hub, which forwards them to a browser on another machine or container.

**Selenium Manager** (built in since Selenium 4.6) finds the browser and downloads the matching driver automatically. You no longer need WebDriverManager or a chromedriver on your PATH.

## Selenium 4 in one table

| Feature | What it means for you |
|---|---|
| W3C protocol only | More consistent behaviour across browsers |
| Selenium Manager | Driver binaries handled for you |
| Relative locators | `RelativeLocator.with(By.tagName("input")).below(label)` |
| Chrome DevTools Protocol access | Network interception, console logs, geolocation (Chromium browsers) |
| New Grid | Single jar, Docker images, observability |

## Set up and run

You need **Java 17+**, **Maven 3.9+** and Chrome, Edge or Firefox.

```bash
python practice-apps/shop-web/shop_web.py      # start the app (port 8091)
cd selenium-java/project
mvn test
```

Settings come from `-D` properties (or environment variables in CI), read by `config/Config.java`:

| Property | Default | Example |
|---|---|---|
| `baseUrl` | `http://127.0.0.1:8091` | `-DbaseUrl=https://staging.example.com` |
| `browser` | `chrome` | `-Dbrowser=edge` |
| `headless` | `true` | `-Dheadless=false` to watch |
| `gridUrl` | empty (local) | `-DgridUrl=http://localhost:4444` |
| `timeoutSeconds` | `10` | `-DtimeoutSeconds=20` |
| `suite` | `testng.xml` | `-Dsuite=testng-parallel.xml` |

> You may see `CdpVersionFinder` warnings in the log. They mean the Selenium version has no DevTools bindings for your exact Chrome version. They're harmless unless you use DevTools features.

## Review questions

**Explain Selenium WebDriver's architecture.**
The client library sends W3C WebDriver commands over HTTP to a browser-specific driver, which drives the real browser. Grid adds a hub that routes sessions to remote browser nodes.

**What is Selenium Manager?**
A tool built into Selenium 4.6+ that detects the browser version and downloads the matching driver, replacing manual driver management and WebDriverManager.

**Selenium IDE vs WebDriver vs Grid?**
IDE records and plays back in the browser (prototyping). WebDriver is the programming API (real frameworks). Grid runs WebDriver sessions in parallel on remote machines or containers.

**What can't Selenium do?**
Test native desktop or mobile apps (use Appium), solve CAPTCHAs (by design), or measure performance under load (use JMeter or k6). It also has no built-in reporting or assertions: that's TestNG/JUnit and libraries like AssertJ.

Next: [2. Locators](02-locators.md)
