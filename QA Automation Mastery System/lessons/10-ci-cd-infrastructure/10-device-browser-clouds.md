# Device & browser clouds

[10 · Module topics](../../modules/10-ci-cd-infrastructure.md) · [Curriculum index](../../README.md)

**Category** · CLOUD TESTING PLATFORMS

You can't own every browser/OS/device combo — platforms rent them per minute: BrowserStack and Sauce Labs (the incumbents), LambdaTest, and AWS Device Farm for real mobile hardware. Your Selenium/Playwright suite points at their remote endpoint with a capabilities payload; they return videos, logs, and screenshots per run. Selenium Grid / Selenoid / Moon are the self-hosted alternative.

## Worked reference

````text
DEVICE & BROWSER CLOUDS — rent the matrix

  HOW IT WORKS (same suite, remote endpoint)
  your test ──▶ hub.browserstack.com/wd/hub
     + capabilities { os, osVersion, browser,
       browserVersion, device, projectName }
  ──▶ real browser/device in their DC
  ◀── results + VIDEO + console/network logs

  THE PLATFORMS
  BrowserStack   broadest real-device fleet; Live
                 (manual) + Automate (suites) + Percy
                 (visual)
  Sauce Labs     the other incumbent; strong
                 analytics, virtual + real devices
  LambdaTest     aggressive pricing, HyperExecute
                 distributed runner
  AWS Device Farm real Android/iOS hardware; pay per
                 device-minute; integrates CodePipeline
  self-hosted    Selenium Grid / Selenoid / Moon on
                 your own VMs/k8s — cheaper at scale,
                 you run it

  REACHING PRIVATE ENVS
  staging isn't public — platforms tunnel:
  BrowserStack Local / Sauce Connect (a secure
  reverse tunnel from your network to theirs)

  CHOOSING
  real devices for touch/sensor/keyboard truth ·
  emulators fine for layout smoke · check parallel
  session pricing — that's the real bill · videos +
  logs retention for triage · SSO/compliance needs

  RULE
  run the FULL matrix nightly in the cloud; keep PR
  gates on 1-2 fast local/containerized browsers.
````

## Exercise

Take one local Playwright or Selenium test and run it on a cloud platform trial against 3 browser/OS combos you can't run locally; compare runtime vs local.
