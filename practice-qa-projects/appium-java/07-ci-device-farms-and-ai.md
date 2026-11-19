# 7. CI, Device Farms and AI-Assisted Mobile Testing

## Mobile tests in CI

Three common setups:

| Setup | How | Good for |
|---|---|---|
| **Emulator on the CI runner** | Linux runner with KVM, an Android emulator action, Appium started in the job | Pull-request smoke tests |
| **Cloud device farm** | Point `appiumUrl` at BrowserStack, Sauce Labs, LambdaTest or AWS Device Farm with their credentials | Many real devices and OS versions |
| **In-house device lab** | Real devices on USB hubs, managed by Appium's device-farm plugin or similar | Teams with specific hardware needs |

Because `DriverFactory` reads `appiumUrl` from configuration, switching from a local server to a cloud farm is a configuration change, plus the provider's capabilities (usually under a vendor prefix such as `bstack:options` or `sauce:options`).

A pipeline for the **verified** path of this tutorial (desktop Chromium through Appium) looks like:

```yaml
- uses: actions/setup-node@v4
  with: { node-version: "22" }
- run: |
    npm install -g appium
    appium driver install --source=npm appium-chromium-driver@2.2.5
    appium --allow-insecure "chromium:chromedriver_autodownload" &
    for i in $(seq 1 30); do curl -sf http://127.0.0.1:4723/status && break; sleep 1; done
- run: python practice-qa-projects/practice-apps/shop-web/shop_web.py &
- run: mvn -B test
  working-directory: practice-qa-projects/appium-java/project
```

(This workflow is an example; it hasn't been run in GitHub as part of this tutorial.)

## Making mobile tests stable

- **Disable animations** on test emulators (Developer options → animation scales off).
- **Start from a known state**: install a fresh build per run, reset or log in through an API or deep link rather than through many screens.
- **Use deep links** to jump straight to the screen under test.
- **Collect evidence**: screenshots, screen recordings (`startRecordingScreen`) and the Appium server log for every failure.
- **Keep suites small on real devices**; push business logic tests down to API and unit level.

## AI-assisted mobile testing

- **Locator help:** AI assistants connected to Appium through an MCP server can read the screen's element tree and suggest robust locators.
- **Self-healing:** when a locator breaks after a UI change, a tool finds the element on the screen where the test failed and proposes a replacement. For example, [k11techlab-appium-qa-mcp](https://github.com/K11-Software-Solutions/k11techlab-appium-qa-mcp) heals Appium locators for Android and iOS on the failing screen, generates Python and Java page objects, re-runs to verify, and checks accessibility.
- **Visual and exploratory checks:** AI can flag layout problems in screenshots across many devices faster than a person scanning them.

Same guardrails as web: review every suggestion, never auto-accept healed locators without an audit trail, and keep real user data out of prompts and device farms.

## Review questions

**How do you run Appium tests in CI?**
Either start an emulator and Appium on the runner (smoke tests), or point the session at a cloud device farm (broad device coverage). Collect screenshots, videos and server logs as artifacts.

**Emulator, simulator or real device?**
Emulators/simulators are fast and cheap for most functional tests. Real devices are needed for performance, hardware features (camera, biometrics, GPS), OEM-specific behaviour and final sign-off.

**How do you reduce mobile flakiness?**
Disable animations, start from a known state (fresh install, API login, deep links), explicit waits on the right condition, native locators instead of XPath, and evidence for every failure.

**What's the risk of self-healing locators?**
Masking a real regression (an element that really disappeared). Healing should propose with evidence and be reviewed.

Next: [8. Review questions and exercises](08-review-questions-and-exercises.md)
