# Compatibility & cross-browser testing

[11 · Module topics](../../modules/11-non-functional-testing.md) · [Curriculum index](../../README.md)

**Category** · OTHER NON-FUNCTIONAL TYPES

Compatibility testing proves the product works across the matrix that matters: browsers (Chromium/Firefox/WebKit/Edge), OSes, devices, screen sizes, and sometimes network speeds. Define a supported matrix from real analytics (don't test everything), emulate the common cases in CI, and use a real-device cloud for the long tail.

## Worked reference

````text
COMPATIBILITY / CROSS-BROWSER TESTING

THE MATRIX (drive it from ANALYTICS, not guesses)
  Browsers   Chrome · Edge · Firefox · Safari(WebKit)
  OS         Windows · macOS · iOS · Android · Linux
  Viewport   mobile · tablet · desktop breakpoints
  Also       screen density/zoom · dark mode · reduced motion ·
             slow network / offline

HOW TO RUN IT
  EMULATION (fast, CI)   Playwright projects per engine;
                         Selenium Grid; device descriptors
  REAL DEVICES (paid)    BrowserStack · Sauce Labs · LambdaTest
                         — real Safari + gestures emulation misses

STRATEGY
  full matrix on release candidates; a 3-browser smoke on every
  PR. pair with visual snapshots to catch render differences.
````

## Exercise

Build a supported-browser matrix from your analytics, then run the same smoke suite across 3 browser engines in CI and one real iOS device in the cloud.
