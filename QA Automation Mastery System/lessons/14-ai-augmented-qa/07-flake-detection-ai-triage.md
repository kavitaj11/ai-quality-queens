# Flake detection & AI triage

[14 · Module topics](../../modules/14-ai-augmented-qa.md) · [Curriculum index](../../README.md)

**Category** · AGENTIC QA, FLAKE AI & MCP

A test that passes on retry is flaky, not fixed. Detect flakes mechanically (retries + report mining), cluster failures by error signature, and hand the clusters to an LLM for root-cause triage.

## Worked reference

````text
# pytest-rerunfailures marks candidates:
#   @pytest.mark.flaky(reruns=3)

# Cluster failures by error signature
from collections import Counter
import re, json

runs = json.load(open("artifacts/report.json"))
sig = lambda err: re.sub(r"\d+", "N",
                         err.splitlines()[-1])
clusters = Counter(sig(t["error"]) for t in runs
                   if t["outcome"] == "failed")
for pattern, count in clusters.most_common(5):
    print(count, pattern)
# Feed the top clusters to an LLM for triage:
# "same root cause? env issue or race condition?"
````

## Exercise

Mine your last test report: list every pass-on-retry test and cluster the failures by normalized error message.
