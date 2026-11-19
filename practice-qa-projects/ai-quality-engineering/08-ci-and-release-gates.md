# 8. CI gates and change impact

The [workflow template](ci/ai-qe.yml) is ready to copy to `.github/workflows/practice-ai-qe.yml` at the repository root. Once installed, it runs on relevant pull requests and pushes to `main`, and can also be started manually. It uses read-only repository permissions, pinned action commits, Python 3.11/3.13, a timeout and seven-day artifact retention. It does not require API secrets or call a model.

## Pipeline stages

1. Verify backend contracts, evaluator logic, structured-judge failure handling, privacy redaction and all ten deliberate defects.
2. Evaluate the golden conversations using three fixed seeds.
3. Compare the candidate with the checked-in offline baseline.
4. Exercise calibration calculations using synthetic fixture predictions.
5. Upload reports and traces even when an earlier stage fails, if those files exist.

Once installed, the workflow makes a check available; merging is blocked only if a repository administrator configures the desired check as required in branch rules. The template does not modify repository protection settings.

## Two gates

The absolute gate checks minimum quality, zero critical failures and every category's pass rate. The comparison gate checks for degradation against a compatible baseline. Dataset, rubric, thresholds, case IDs, seeds and judge identity must match. A comparison must fail clearly rather than silently compare different populations or loosened gates.

```bash
python -m aiqe run --repeats 3 --seed 7 --output artifacts/current
python -m aiqe compare
python -m aiqe run --fault hallucination --output artifacts/negative
python -m aiqe compare --candidate artifacts/negative/report.json
```

The first two commands exit 0; the last two exit 1. In shell automation, capture the expected negative-control exit explicitly. Do not add `continue-on-error` to the real quality gate.

## What to run for each change

| Change | Fast checks | Broader evidence before release |
|---|---|---|
| Prompt | Golden regression, injection and benign controls | Multi-turn repeats and semantic adjudication on affected journeys |
| Model version | Entire suite and contract/schema validation | Paired evaluation, fresh safety assessment, latency/cost measurements |
| Retrieval or knowledge | Document availability, citations and corpus checks | Faithfulness/completeness by topic, outdated/conflicting source tests |
| Backend API | Direct contracts and negative responses | Tool planning, authorization, idempotency and failure recovery |
| Judge or rubric | Structured result tests and calibration | Human agreement, frozen holdout review and a new comparable baseline |

The template runs the whole small lab for any project change; it does not implement production risk-based test selection. A nightly or pre-release live evaluation can be added using protected secrets, fixed budgets and reviewed data. Never expose secrets to untrusted fork code or run arbitrary pull-request code in a privileged trigger.

**When is rebaselining legitimate?** After a reviewed change to the evaluation population or contract, with the old evidence retained. It is not a way to turn a regression green automatically.

**What if a semantic score is uncertain?** Keep the release decision separate from the raw score. A review-required state should block unattended promotion for the affected risk tier; do not convert missing or uncertain evidence into a pass.

Next: [Technical practice](09-practice-and-exercises.md).

