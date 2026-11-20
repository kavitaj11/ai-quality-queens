# Evals as a GitHub App: LLM Eval Agent

[16 · Module topics](../../modules/16-ai-testing-llm-evaluation.md) · [Curriculum index](../../README.md)

**Category** · EVALS IN CI/CD

The app approach: a GitHub App (e.g. LLM Eval Agent — github.com/K11-Software-Solutions/llm-eval-agent-app) is a hosted service the repo installs. GitHub sends it webhooks on PR events; the app runs the eval harness on its own infrastructure and reports back through the Checks API — a check run with per-case annotations and a summary comment on the PR. One installation covers many repos, keys live server-side, and eval logic/datasets are managed centrally instead of copy-pasted YAML.

## Worked reference

````text
# Core of an eval GitHub App (FastAPI + Checks API)
@app.post("/webhook")
async def on_webhook(req: Request):
    verify_signature(req)               # X-Hub-Signature-256
    event = await req.json()
    if event.get("action") in ("opened", "synchronize"):
        pr = event["pull_request"]
        token = installation_token(event["installation"]["id"])
        results = run_eval_suite(pr["head"]["sha"])   # your harness
        post_check_run(token, pr, results)

def post_check_run(token, pr, results):
    requests.post(
        f"{API}/repos/{pr['base']['repo']['full_name']}/check-runs",
        headers={"Authorization": f"Bearer {token}"},
        json={
            "name": "llm-eval-agent",
            "head_sha": pr["head"]["sha"],
            "conclusion": "success" if results.passed else "failure",
            "output": {
                "title": f"pass rate {results.pass_rate:.0%}",
                "summary": results.markdown_report(),
            },
        })
# required check + branch protection = eval gate on every PR
````

## Exercise

Trace the full round-trip: PR opened → webhook → app authenticates as installation → runs evals → posts check run. Where do API keys live at each step?
