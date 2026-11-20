# Using existing MCP servers

[15 · Module topics](../../modules/15-autonomous-qa-pipelines.md) · [Curriculum index](../../README.md)

**Category** · MCP INFRASTRUCTURE & GUARDRAILS

Before building anything, compose what exists: Playwright MCP (browser control via accessibility tree), GitHub MCP (PRs, issues, checks, code search), Jira/Atlassian MCP (tickets, boards), filesystem MCP, database MCPs, Slack MCP. A working autonomous QA loop is mostly off-the-shelf servers wired together — you add a custom qa-tools server only for what's uniquely yours (your test runner, your fixtures, your reports).

## Worked reference

````text
OFF-THE-SHELF MCP SERVERS — compose before you build

  SERVER          GIVES AGENTS              QA USE
  Playwright MCP  real browser: navigate,   exploratory
  (@playwright/   click, fill, snapshot     agents · repro
   mcp)           (accessibility tree —     attempts · smoke
                  robust, not pixels)       checks
  GitHub MCP      PRs, diffs, issues,       read the diff to
                  checks, code search       plan tests · file
                                            bugs · open heal
                                            PRs · post checks
  Jira /          tickets, boards,          triage agent files
  Atlassian MCP   comments, transitions     + dedupes tickets;
                                            links runs to
                                            stories
  filesystem MCP  read/write project        datasets, reports,
                  files (scoped dirs!)      artifacts
  database MCPs   query test DBs            seed verification,
                  (postgres, sqlite…)       state assertions
  Slack MCP       post messages             escalations +
                                            run summaries

  THE COMPOSED LOOP (no custom code yet)
  GitHub MCP: read PR diff
    → Playwright MCP: explore changed screens
    → Jira MCP: file triaged bug with repro
    → Slack MCP: summary to #qa

  BUILD CUSTOM ONLY FOR WHAT'S YOURS
  run_suite · seed_fixture · get_flake_history —
  the next lesson. Everything generic: reuse.

  VETTING A THIRD-PARTY SERVER (it runs with YOUR creds)
  • who publishes it? official > community fork
  • what scopes does it demand? (Jira write? repo
    admin?) — grant per-agent minimums, see Guardrails
  • pin versions — a server update is a supply-chain
    change to your pipeline
  • read its tool descriptions: they enter your
    agents' prompts verbatim (injection surface)
````

## Exercise

Wire Playwright MCP + GitHub MCP into an AI assistant and run one loop end-to-end: explore a staging page, find an issue, and file it as a GitHub issue with repro steps. Note every permission you had to grant.
