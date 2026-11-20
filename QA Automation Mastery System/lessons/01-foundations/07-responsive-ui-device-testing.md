# Responsive UI & device testing

[01 · Module topics](../../modules/01-foundations.md) · [Curriculum index](../../README.md)

**Category** · WEB & HTTP ESSENTIALS

Real users arrive on phones, tablets, and desktops across many browsers — QA has to prove the UI works at every breakpoint and input mode. Test the responsive breakpoints (mobile/tablet/desktop), touch vs mouse, orientation, and cross-browser rendering. Automation drives this with viewport sizing and device emulation (fast, in CI) plus a real-device cloud lab for the cases emulation can't catch.

## Worked reference

````text
RESPONSIVE & CROSS-DEVICE TESTING — the map

WHAT TO COVER
  Breakpoints   mobile ~375 · tablet ~768 · desktop ≥1024
  Input mode    touch (tap, swipe, pinch) vs mouse + keyboard
  Orientation   portrait ↔ landscape
  Browsers      Chromium · Firefox · WebKit(Safari) · Edge
  OS / device   iOS Safari & Android Chrome behave differently
  Density/zoom  retina scaling, browser zoom, large-font a11y

WHAT TO ASSERT AT EACH BREAKPOINT
  ✓ layout reflows (nav → hamburger, columns → stack)
  ✓ NO horizontal scroll / clipped content
  ✓ tap targets ≥ 44×44px (mobile a11y)
  ✓ images/text scale; nothing overlaps
  ✓ core flow still completes (add to cart → checkout)

TWO WAYS TO RUN IT
  1. EMULATION (fast, free, in CI)
     set the viewport + device descriptor and drive normally.
     Playwright ships device profiles (iPhone, Pixel) with the
     right viewport, userAgent, touch, and DPR baked in.
       Playwright:  use: { ...devices['iPhone 15'] }
       Selenium:    Chrome mobileEmulation / --window-size
     Great for layout + flow. It is NOT a real device.

  2. REAL-DEVICE CLOUD (accurate, paid)
     BrowserStack · Sauce Labs · LambdaTest — actual iOS/Android
     hardware and real Safari. Catches gesture, rendering, and
     performance issues emulation misses. Point RemoteWebDriver
     / Playwright at the grid; run the SAME tests.

WHERE THIS SITS
  emulate the matrix on every PR (cheap); run the real-device
  slice nightly / pre-release. Pair with responsive VISUAL
  snapshots to catch layout regressions automatically.

  → For the concrete viewport / device-emulation code, see the
    Playwright phase (contexts, devices, and emulation).
````

## Exercise

Pick one page and verify it at 3 viewports (375×667, 768×1024, 1440×900): assert the mobile nav collapses to a hamburger, no horizontal scroll, and tap targets are ≥ 44px.
