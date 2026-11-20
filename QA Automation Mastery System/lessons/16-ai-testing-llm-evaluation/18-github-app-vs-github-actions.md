# GitHub App vs GitHub Actions

[16 · Module topics](../../modules/16-ai-testing-llm-evaluation.md) · [Curriculum index](../../README.md)

**Category** · EVALS IN CI/CD

Same gate, different trade-offs. Actions: config lives in the repo, runs on GitHub runners, keys in repo secrets, per-repo setup — transparent and free to start, but eval logic drifts across repos. App: install once org-wide, compute and keys on your server, centralized datasets/judges, richer reporting via the Checks API — but you operate a service. Rule of thumb: Actions for one team/repo; an App when evals are a platform serving many repos. They compose: an Action for the cheap smoke set, the App for the full centralized suite.

## Worked reference

````text
GITHUB APP  vs  GITHUB ACTIONS — for LLM eval gating

                    ACTIONS (workflow)     APP (e.g. LLM Eval Agent)
  setup             YAML per repo          install once, org-wide
  trigger           on: events in repo     webhooks pushed to server
  compute           GitHub-hosted runners  your own infrastructure
  API keys          repo/org secrets       server-side only
  eval logic        versioned in repo      centralized service
  identity          GITHUB_TOKEN of run    app's own bot identity
  permissions       broad job token        fine-grained per-install
  reporting         job pass/fail + logs   Checks API: annotations,
                                           rich summaries, comments
  cross-repo        copy-paste / reusable  one deploy updates all
    consistency       workflows drift        installs at once
  cost              runner minutes         you host; API calls free
  ops burden        none                   you run a service

  CHOOSE ACTIONS   one repo/team · logic next to code ·
                   zero infra · getting started
  CHOOSE AN APP    many repos, one standard · central datasets
                   & judges · keys must not live in repos ·
                   rich PR feedback (per-case annotations)
  HYBRID           Action runs smoke set per PR;
                   App runs full suite + posts the check
````

## Exercise

Your org has 12 repos with LLM features and one shared golden dataset. Argue for App vs Actions — then design the hybrid.
