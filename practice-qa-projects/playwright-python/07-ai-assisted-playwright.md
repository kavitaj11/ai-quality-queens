# 7. AI-Assisted Playwright Testing

Playwright is the tool most AI testing agents use to drive a browser, so this is where AI-assisted testing is moving fastest.

## Where AI helps today

| Task | How | Review responsibility |
|---|---|---|
| **Writing tests** | Copilot / Claude Code drafts tests and page objects in your project's style | Review assertions against the requirement |
| **User story → test** | An agent reads acceptance criteria, explores the app in a real browser, and writes a test with verified locators | Check it tests the behaviour, not just the happy path |
| **Locator healing** | When a locator breaks, a tool proposes a new one with a confidence score and the reason | Review and commit; never auto-heal silently |
| **Failure triage** | Summarise a trace, error and screenshot into a likely cause | Confirm in the trace viewer |
| **Test data** | Generate varied, synthetic data and edge cases | Keep real customer data out |

## MCP: giving an AI assistant a browser

**Model Context Protocol (MCP)** servers expose tools to AI assistants such as Claude Code. A Playwright MCP server lets the assistant open pages, read the accessibility tree, click, fill and assert, so it can **check its own test against the real app** instead of guessing locators.

For example, [k11techlab-playwright-qa-mcp](https://github.com/K11-Software-Solutions/k11techlab-playwright-qa-mcp) turns user stories into tests with verified locators, runs them with structured results, and proposes self-healing locators with confidence and reasons.

A typical loop:

```
user story ──► agent explores the page (accessibility tree, test IDs)
           ──► agent writes a test using page objects
           ──► agent runs it ──► fails? reads the error/trace, fixes, re-runs
           ──► human reviews the pull request
```

## Prompting for good tests

Give the assistant the context it can't guess:

- Your conventions: "use the page objects in `pages/`; locators by role, then test ID; `expect()` not `assert` for page state".
- The requirement, including negative cases and boundaries.
- What *not* to do: "no `wait_for_timeout`, no CSS selectors on classes, no test depending on another".
- Ask for **test ideas first**, review them, then ask for code.

## Guardrails

- **Review like a pull request from a new teammate.** AI-written tests often pass for the wrong reason: asserting what the page shows today rather than what the requirement says.
- **Break it on purpose.** Change the expected value or the app and confirm the test fails, as this tutorial's negative control did.
- **No silent healing.** A healed locator can hide a real regression (the button really is missing). Healing should propose, explain and wait for approval.
- **Keep data private.** Don't paste credentials, tokens or customer data into prompts; use approved tools only.

## Review questions

**How have you used AI in test automation?**
Give a concrete example: drafting page objects or tests in the framework's style, generating edge-case data, or triaging failures. Then say how you reviewed and verified the output.

**What is a self-healing locator, and what's the risk?**
A tool that finds a replacement when a locator stops matching. The risk is masking real UI regressions, so healing must be reviewed and logged.

**What is MCP and why does it matter for testing?**
A standard way to expose tools to AI assistants. With a browser-automation MCP server, an assistant can explore the live app and verify the tests it writes instead of guessing.

**Would you trust AI-generated tests in CI?**
Only after review, and only if they've been shown to fail when the behaviour breaks. The quality bar is the same as for human-written tests.

Next: [8. Review questions and exercises](08-review-questions-and-exercises.md)
