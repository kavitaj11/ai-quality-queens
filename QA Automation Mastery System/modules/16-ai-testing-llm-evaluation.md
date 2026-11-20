# 16 · AI Testing & LLM Evaluation

[Curriculum index](../README.md) · [Weekly planner](../planner.md)

**Study window** · 23–24

## What to study

Build evaluation datasets and rubrics, compare deterministic checks with model judges, and evaluate retrieval separately from generation. Connect eval results to CI gates or GitHub App checks.

## Practice assignment

Create a case where retrieval misses the supporting document and another where the answer misuses a retrieved document. Score and report them separately.

## Topics and lessons

### FOUNDATION

- [AI app architecture: what you're actually testing](../lessons/16-ai-testing-llm-evaluation/01-ai-app-architecture-what-you-re-actually-testing.md)
- [Evaluation criteria: what 'good' means](../lessons/16-ai-testing-llm-evaluation/02-evaluation-criteria-what-good-means.md)
- [LLM failure modes: a taxonomy](../lessons/16-ai-testing-llm-evaluation/03-llm-failure-modes-a-taxonomy.md)
- [Setting up an eval harness](../lessons/16-ai-testing-llm-evaluation/04-setting-up-an-eval-harness.md)

### FUNDAMENTALS OF TESTING AI

- [Why testing AI is different](../lessons/16-ai-testing-llm-evaluation/05-why-testing-ai-is-different.md)
- [The AI-app test pyramid](../lessons/16-ai-testing-llm-evaluation/06-the-ai-app-test-pyramid.md)
- [Building an eval dataset](../lessons/16-ai-testing-llm-evaluation/07-building-an-eval-dataset.md)

### LLM EVALUATION

- [Evaluation methods & metrics](../lessons/16-ai-testing-llm-evaluation/08-evaluation-methods-metrics.md)
- [LLM-as-a-judge](../lessons/16-ai-testing-llm-evaluation/09-llm-as-a-judge.md)
- [Eval frameworks & CI gating](../lessons/16-ai-testing-llm-evaluation/10-eval-frameworks-ci-gating.md)
- [Safety, red-teaming & guardrails](../lessons/16-ai-testing-llm-evaluation/11-safety-red-teaming-guardrails.md)

### RAG EVALUATION

- [Evaluating retrieval](../lessons/16-ai-testing-llm-evaluation/12-evaluating-retrieval.md)
- [Evaluating generation: the RAG triad](../lessons/16-ai-testing-llm-evaluation/13-evaluating-generation-the-rag-triad.md)
- [End-to-end RAG eval & regression](../lessons/16-ai-testing-llm-evaluation/14-end-to-end-rag-eval-regression.md)

### EVALS IN CI/CD

- [How LLM evals run in a pipeline](../lessons/16-ai-testing-llm-evaluation/15-how-llm-evals-run-in-a-pipeline.md)
- [Eval gates with GitHub Actions](../lessons/16-ai-testing-llm-evaluation/16-eval-gates-with-github-actions.md)
- [Evals as a GitHub App: LLM Eval Agent](../lessons/16-ai-testing-llm-evaluation/17-evals-as-a-github-app-llm-eval-agent.md)
- [GitHub App vs GitHub Actions](../lessons/16-ai-testing-llm-evaluation/18-github-app-vs-github-actions.md)
