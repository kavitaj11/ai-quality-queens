# AI evaluation and agent traces

**Hands-on project** · [AI evaluation and agent traces lab](../../../../practice-qa-projects/ai-quality-engineering/README.md)

**QA Automation Mastery** · [16 · AI Testing & LLM Evaluation](../../../../QA%20Automation%20Mastery%20System/modules/16-ai-testing-llm-evaluation.md)

Did the agent take the expected action and support its answer?

## Evaluate the answer, the evidence and the action

Start by defining the unit of evaluation. For a conversational system it may be one answer or a sequence of turns. For an agent it may also include tool selection, arguments and the resulting state change. A fluent confirmation tells you what the agent said. A tool trace records what it requested. A receipt or independent state check establishes what actually happened.

Use deterministic checks for exact invariants, such as one scheduled job for one approved request. For qualities such as relevance or groundedness, define a rubric with examples and compare judge decisions with human-reviewed labels. Inspect disagreements by task type and retain the model, prompt and dataset versions. An overall average can improve while performance on cases requiring a tool call gets worse.

## What to test

Use a labeled synthetic case with an expected action, permitted tools and reference evidence. Review the tool trace separately from the answer. A plausible confirmation can conceal a missing tool call.

## Practice assignment

Follow the setup in the linked lab. Compare two runs on the same cases. Inspect one changed verdict, record the evidence that supports it, and explain whether the difference arose from system behavior or the grader.

[Tools and technologies study guide](README.md) · [All practice QA projects](../../../../practice-qa-projects/)
