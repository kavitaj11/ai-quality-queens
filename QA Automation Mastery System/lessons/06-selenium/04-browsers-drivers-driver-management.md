# Browsers, drivers & driver management

[06 · Module topics](../../modules/06-selenium.md) · [Curriculum index](../../README.md)

**Category** · WEBDRIVER CORE

Selenium doesn't talk to a browser directly — it talks to that browser's driver, a small executable implementing the W3C endpoint (chromedriver, geckodriver, msedgedriver, safaridriver). The classic pain is version drift: the driver must match the installed browser. Selenium Manager (built into Selenium 4.6+) now auto-resolves and downloads the right driver, so manual PATH juggling and WebDriverManager are mostly history.

## Worked reference

````text
from selenium import webdriver
from selenium.webdriver.chrome.options import Options

# ── Selenium 4.6+ : Selenium Manager auto-resolves drivers ──
# No path, no webdriver-manager pip package needed:
chrome  = webdriver.Chrome()
firefox = webdriver.Firefox()
edge    = webdriver.Edge()

# ── Options (headless, window size, insecure certs) ──
opts = Options()
opts.add_argument("--headless=new")
opts.add_argument("--window-size=1920,1080")
opts.set_capability("acceptInsecureCerts", True)
driver = webdriver.Chrome(options=opts)

# ── Pin a browser version ──
opts.browser_version = "120"

# ── Explicit driver path (air-gapped CI) ──
from selenium.webdriver.chrome.service import Service
driver = webdriver.Chrome(
    service=Service(executable_path="/opt/chromedriver"),
    options=opts)

# ── Remote (Grid / cloud lab) — same options object ──
remote = webdriver.Remote(
    command_executor="http://grid:4444/wd/hub", options=opts)
````

## Exercise

Launch Chrome, Firefox, and Edge in one test run letting Selenium Manager fetch drivers automatically; then pin a specific chromedriver version and observe the 'session not created' mismatch error.
