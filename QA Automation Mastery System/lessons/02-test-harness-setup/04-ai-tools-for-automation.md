# AI tools for automation

[02 · Module topics](../../modules/02-test-harness-setup.md) · [Curriculum index](../../README.md)

**Category** · TOOLCHAIN

A fast-moving category of tools that use AI/ML to cut the two biggest costs in UI automation: writing tests and maintaining locators. They cluster into self-healing (locators auto-repair when the DOM shifts), codeless/AI authoring (record or describe a flow, the tool generates it), and AI copilots/agents (LLMs draft, triage, and explore). Know the landscape and the golden rule: AI drafts and heals, humans review — silent healing can hide real regressions.

## Worked reference

````text
AI TOOLS FOR TEST AUTOMATION — the landscape

┌─ SELF-HEALING LOCATORS (auto-repair when the DOM changes) ─┐
  Healenium     open-source · wraps Selenium WebDriver · stores
                a locator history and heals to the closest node;
                logs every heal for review.  (Java-first)
  Testim        AI "Smart Locators" score many attributes so a
                changed id/class doesn't break the step.
  Mabl          low-code SaaS; auto-heals + auto-detects visual
                and performance regressions.
  Functionize   ML-based element identification + NL authoring.
  Note: Selenium/Playwright have NO built-in healing — you add
        it via these tools or a ranked fallback-locator chain.

┌─ CODELESS / AI AUTHORING (generate tests, less code) ──────┐
  Testim        record or describe -> stable, editable tests.
  Mabl          record flows in-browser; runs cross-browser.
  Katalon (StudioAssist) · TestCraft · Ranorex — record + AI.
  Playwright codegen — record-and-generate (not AI, but the
                free baseline many teams start from).

┌─ AI COPILOTS & AGENTS (LLM-driven, this course's focus) ───┐
  Playwright MCP  gives an AI agent a real browser (a11y tree)
                  to explore and draft specs.
  GitHub Copilot / Cursor / Claude — draft page objects, tests,
                  and triage failures from stack traces.
  ZeroStep, Auto Playwright — natural-language steps in a run.
  Applitools Eyes — AI VISUAL testing (perceptual diffing).

┌─ WHERE THEY FIT vs OPEN SOURCE ────────────────────────────┐
  Selenium / Playwright  = free, code-first, full control,
                           you own healing + authoring.
  AI/commercial tools    = faster authoring + auto-maintenance,
                           license cost, less control, vendor
                           lock-in, results need review.

THE GOLDEN RULE
  AI generates, heals, and triages — a HUMAN reviews and commits.
  • log every self-heal and inspect it: a "healed" locator may
    be masking a real, user-facing regression.
  • generated tests are a DRAFT — review like any pull request.
  • quarantine new AI tests until they pass N consecutive runs.
````

## Exercise

Pick one self-healing tool and one AI-authoring tool. For each, list what it automates, where it plugs into your stack, and one risk you'd guard against.
