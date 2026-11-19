# 9. AI-Augmented Performance Testing

**AI-augmented** means a person still runs the testing and owns the decisions, and an AI assistant (Claude, GitHub Copilot and similar) speeds up the slow parts. The skills from Chapters 1–8 stay essential, because you have to judge whether the AI is right.

## Where AI helps, and where it doesn't

| Task | How AI helps | What you still must do |
|---|---|---|
| **Workload modeling** | Summarize access logs or analytics exports into journeys, mix and peak rates | Check the numbers against the source; confirm with the product owner |
| **Script creation** | Draft a JMeter plan or test steps from an OpenAPI spec or a HAR recording | Open it in the GUI, run with 1 user, check every assertion and timer scope |
| **Finding correlation** | Spot dynamic values (tokens, IDs) by comparing two recordings | Verify each extractor with the JSON Path Tester |
| **Test data** | Generate realistic, varied, synthetic data (no real customer data) | Check volumes and edge cases |
| **Results analysis** | Summarize runs, point at the knee, suggest likely causes and next tests | **Check every claim against the numbers** |
| **Reporting** | Draft a readable report for managers from your findings | Make sure it says what the data says, no more |
| **Anomaly spotting** | Flag unusual patterns across many runs (drift, new error types) | Investigate before believing |

AI is weak at: knowing your system's internals, knowing what "normal" is without history, and admitting when the evidence is thin. It can describe a plausible-sounding cause that the data doesn't support. That's why the prompt below explicitly asks it to say when evidence is missing.

## Rule 1: control what you share

Load test results can contain user names, tokens, internal URLs and response bodies. Before sending anything to an AI tool:

- Use only **approved** tools (your company's policy decides, not convenience).
- Send **aggregated numbers**, not raw `.jtl` files or responses.
- Never paste tokens, passwords or customer data.

`scripts/ai_summary.py` follows these rules. By default it **does not call any AI**. It builds the prompt from the `summary.json` files (aggregates only: counts, percentiles, error codes, timeline) and saves it to `prompt.md` so you can read exactly what would be shared.

## Try it

After running the agent in Chapter 10, use its results folder (replace `agent-20261116-172724` with yours):

```bash
RUN=results/agent-20261116-172724

# 1. Build the prompt only (free, nothing leaves your machine)
python scripts/ai_summary.py $RUN/step-*/summary.json --server-metrics $RUN/server_metrics.json --out-dir $RUN

# 2. Read $RUN/prompt.md, then either paste it into your approved assistant, or:
pip install -r requirements-ai.txt
python scripts/ai_summary.py $RUN/step-*/summary.json --server-metrics $RUN/server_metrics.json --out-dir $RUN --call
```

The agent can also do this for you at the end of its run with `--ai`.

`--call` sends the prompt to Claude through the Anthropic API. You need an API key (`ANTHROPIC_API_KEY`) or an `ant auth login` profile, and each call is billed to your account.

The real prompt built from this tutorial's step test is about 12,000 characters and contains no user names, tokens or response bodies.

## Writing a good analysis prompt

The prompt in `ai_summary.py` uses patterns that work for any analysis task:

1. **A role and a constraint:** "senior performance engineer… use only the numbers provided… do not invent metrics."
2. **An escape hatch:** "when the evidence is not enough to name a cause, say what extra measurement would settle it." Without this, models tend to guess.
3. **A fixed structure:** verdict, capacity, likely bottleneck, next tests, caveats. That makes answers comparable run to run.
4. **The audience and length:** "under 350 words, for a beginner."
5. **Structured data:** JSON with clear field names, sorted by load.

## Check the AI's answer: a worked check

For the step test in Chapter 7, a correct analysis must say:

- 25, 50 and 100 users passed; 200 failed on checkout p95 (836 ms > 800 ms).
- The knee is between 100 and 200 users: throughput 89 → 119 req/s (1.33×) while p95 rose 110 → 790 ms.
- The likely bottleneck is the connection pool: average pool wait rose from 0.06 ms to 490 ms, with up to 101 requests waiting.
- The 200-user errors start with connection refused, which points to the accept queue as a second cause.

If an AI answer misses the connection-refused errors, blames the CPU (which nobody measured) or quotes a number that isn't in the data, you've just seen why **you** sign off the report, not the AI.

## Where this is heading

AI-augmented tools are moving into the load testing tools themselves: generating scripts from traffic, explaining dashboards in plain language, and comparing runs automatically. The next step is letting an AI *run* tests, not only read them. That's agentic QA, in Chapter 10.

Next: [10. Agentic QA for performance](10-agentic-qa-for-performance.md)
