# Selenium: origins & history

[06 · Module topics](../../modules/06-selenium.md) · [Curriculum index](../../README.md)

**Category** · WEBDRIVER CORE

Context matters in interviews. Selenium began in 2004 when Jason Huggins at ThoughtWorks built a JS tool ('Selenium Core') to test an internal app. Its story is the story of browser automation itself — from injected JavaScript, to a proxy server, to a native W3C browser standard.

## Worked reference

````text
SELENIUM — A TIMELINE

2004  Selenium Core (Jason Huggins, ThoughtWorks)
      • JavaScript injected into the page to drive it
      • Limited by the browser's JS "same-origin" sandbox

2006  Selenium RC (Remote Control)
      • A server proxied commands to work around the sandbox
      • Slow: every command was an HTTP round-trip through JS

2007  WebDriver (Simon Stewart, Google)
      • A separate project: drive the browser NATIVELY
      • via each browser's own automation hooks — no JS proxy

2009  THE MERGER  →  "Selenium WebDriver" (Selenium 2)
      • RC + WebDriver combine; WebDriver becomes the API
      • Communicated over the JSON Wire Protocol

2016  Selenium 3
      • RC officially deprecated; WebDriver-only
      • Vendors ship their own drivers (chromedriver, geckodriver)

2018  W3C WebDriver becomes a web STANDARD
      • Browser automation is now an official spec,
        not a reverse-engineered protocol

2021  Selenium 4
      • Fully W3C WebDriver (JSON Wire dropped)
      • DesiredCapabilities → Options classes
      • Relative locators, CDP access, window/tab APIs

2023+ Selenium + WebDriver BiDi
      • Bidirectional, event-driven, cross-browser standard
      • Console/network/log streaming without vendor-only CDP

WHY IT STILL MATTERS
  Largest ecosystem • most job listings • every language •
  every browser • the foundation the W3C standard was built on.
````

## Exercise

Explain to a teammate why Selenium 4 dropped the JSON Wire Protocol and what changed for them in practice (spoiler: desired capabilities → Options).
