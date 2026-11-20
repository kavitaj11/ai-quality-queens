# LLM-as-a-judge

[16 · Module topics](../../modules/16-ai-testing-llm-evaluation.md) · [Curriculum index](../../README.md)

**Category** · LLM EVALUATION

When there's no single right answer, use a strong LLM to grade output against a rubric — scalable and surprisingly aligned with humans when done right. The craft: a specific rubric, a low-temperature judge, structured JSON scores with reasons, and pairwise comparison to reduce bias. Always validate the judge against human labels, and beware its biases (position, verbosity, self-preference).

## Worked reference

````text
import anthropic, json
client = anthropic.Anthropic()

JUDGE = """You are a strict QA evaluator. Score the ASSISTANT
reply against the RUBRIC. Reply ONLY JSON:
{{"faithfulness": 1-5, "helpfulness": 1-5,
  "policy_accurate": true/false, "reason": "..."}}

RUBRIC: must cite the 30-day refund window, be polite,
and never promise a refund outside policy.

USER: {q}
ASSISTANT: {a}"""

def judge(q, a):
    msg = client.messages.create(
        model="claude-sonnet-4-5", max_tokens=300,
        temperature=0,                 # low temp = repeatable grades
        messages=[{"role":"user",
                   "content": JUDGE.format(q=q, a=a)}])
    return json.loads(msg.content[0].text)

score = judge(case["input"], model_output)
assert score["faithfulness"] >= 4 and score["policy_accurate"]

# GOOD JUDGE HYGIENE
#  • specific rubric > "is this good?"   • temperature 0
#  • ask for a REASON (improves scores + debuggable)
#  • PAIRWISE ("is A or B better?") beats absolute scoring
#  • VALIDATE the judge vs human labels before trusting it
#  • known biases: position, verbosity, self-preference
````

## Exercise

Write a judge prompt that scores a support reply 1–5 on helpfulness and policy-accuracy, returns JSON, and give it 5 outputs to grade. Spot-check against your own rating.
