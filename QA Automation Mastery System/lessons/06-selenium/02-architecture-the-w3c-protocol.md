# Architecture & the W3C protocol

[06 · Module topics](../../modules/06-selenium.md) · [Curriculum index](../../README.md)

**Category** · WEBDRIVER CORE

How a line of your code becomes a click in a browser. Your test calls a language binding, which sends W3C WebDriver commands as HTTP requests to a browser driver (chromedriver/geckodriver), which uses the browser's native automation hooks and sends a response back. Understanding this request→response chain explains almost every Selenium error you'll ever see.

## Worked reference

````text
THE WEBDRIVER CHAIN (request → response, one command at a time)

   ┌─────────────────┐
   │   YOUR TEST     │   driver.findElement(By.id("email"))
   │  (Py/JS/Java)   │
   └────────┬────────┘
            │  calls
            ▼
   ┌─────────────────┐
   │ LANGUAGE BINDING│   selenium-webdriver library
   │  (the client)   │   turns the call into a W3C command
   └────────┬────────┘
            │  HTTP  POST /session/{id}/element
            │  body: {"using":"css selector","value":"#email"}
            ▼
   ┌─────────────────┐
   │ BROWSER DRIVER  │   chromedriver / geckodriver / msedgedriver
   │ (W3C endpoint)  │   a local HTTP server implementing the spec
   └────────┬────────┘
            │  native automation hooks (DevTools / Marionette)
            ▼
   ┌─────────────────┐
   │    BROWSER      │   Chrome / Firefox / Edge / Safari
   └────────┬────────┘
            │  finds the element, returns a reference
            ▼
       RESPONSE travels back up the SAME chain:
       {"value":{"element-6066-...":"<uuid>"}}  →  WebElement


WHY THIS EXPLAINS YOUR ERRORS
  • "session not created"    → driver ↔ browser version mismatch
  • connection refused       → driver process not running/starting
  • stale element reference  → the returned reference no longer
                               maps to a live DOM node (page changed)
  • it's HTTP + synchronous  → each command blocks for a full
                               round-trip (why chatty tests are slow)

W3C WebDriver = the standard these drivers all implement, so the
SAME test code drives any compliant browser. (Selenium 4 dropped
the older JSON Wire Protocol for this spec.)
````

## Exercise

Enable driver logging and match a single findElement call to the exact HTTP request/response pair it produced.
