# ♛ ai-quality-queens

[![AI Quality Queens — Building expertise. Advancing trustworthy AI.](assets/aiqq-banner.webp)](https://kavitaj11.github.io/ai-quality-queens/#/)

**AI Quality Queens** is a community learning guide for testers, from designing a first boundary test to evaluating an AI agent’s decisions. The QA Queens here are our sisters and mothers. They are also the colleagues who review a first test script, share what they learned from a failed run, or make time for someone rebuilding a career. Our primary goal is to help the QA community while challenging ourselves to learn something each day.

## Choose your starting position

| Level | Responsibilities |
|---|---|
| Beginner | **Design a test.** Pick a business rule, write down what should happen at its boundary, and explain how you would recognize a defect. [Test automation fundamentals](qa_foundational_knowledge/quality-engineering/test-automation-fundamentals/README.md) |
| Developing | **Automate a check.** Turn that boundary into a repeatable test. Change the input by one unit and check whether the failure message tells you what went wrong. [Java tutorials and examples](qa_foundational_knowledge/quality-engineering/test-automation-in-java/README.md) · [Python and pytest](qa_foundational_knowledge/quality-engineering/test-automation-in-python/README.md) · [Data structures and algorithms in Java](qa_foundational_knowledge/DSA-training/README.md) |
| Intermediate | **Test across systems.** Follow one order from reservation through shipment. Decide which checks belong at the API, browser, or database layer and what evidence connects them. [Domain knowledge exercises](domain_knowledge_exercises/README.md) · [Nine hands-on QA projects](practice-qa-projects/README.md) |
| Advanced | **Evaluate AI and agents.** Compare an AI-generated explanation with measured results. Inspect an agent’s trace to check whether it chose an appropriate tool and when it decided to stop. [AI-augmented performance testing](practice-qa-projects/performance-testing-jmeter/09-ai-augmented-performance-testing.md) · [Agentic QA for performance](practice-qa-projects/performance-testing-jmeter/10-agentic-qa-for-performance.md) · [AI quality engineering lab](practice-qa-projects/ai-quality-engineering/README.md) |

[Explore the quality engineering guides](qa_foundational_knowledge/quality-engineering/README.md) · [Read about Kavita’s testing experience](qa_foundational_knowledge/README.md)

## The queen’s strategy for AI quality

AI systems can be nondeterministic. Give an assistant the same checkout requirement twice and it may generate different test cases, choose different tools, or reach the same verdict for different reasons. One successful run tells you what happened once. To judge reliability, repeat the task under recorded conditions and ask which outcomes must hold on every run.

That is where the queen’s range matters. **Move along one line** by holding the input, model version, and tool setup fixed and comparing repeated runs. **Change direction** by varying one condition, such as the cart subtotal, to see where the verdict changes. **Follow the diagonal across systems** by tracing the assistant’s claim through its tool calls to the customer’s invoice. Each move answers a different question: how often does the behavior recur, what triggers it, and did the system actually do what the assistant says?

Try those moves on one puzzle. An assistant marks checkout as passing because the screen shows “Free shipping,” yet the invoice includes a fee. The assistant’s path may vary. The shipping rule gives you a stable expectation against which to judge every path.

1. **Opening · Establish the rule.** Shipping is free on subtotals of $50 or more. Before running a test, predict the fee at $49.99, $50.00, and $50.01. Ask an AI assistant to propose cases, then compare its choices with yours. If it skips exactly $50.00, which pricing defect could slip through?
2. **Middlegame · Choose the next check.** For the same $50 order, the browser displays “Free shipping.” Inspect the quote API. If it returns a fee, investigate why the display and quote disagree. If it returns zero, follow the order ID into billing and find where the fee first appears. In either position, one observation narrows your next move.
3. **Endgame · Decide whether the fix holds.** Replay all three boundary cases on the corrected build, then reconcile the checkout quote with the invoice for the original failing case. If an agent declares the fix successful, inspect its trace for that invoice check. A green pricing suite alone leaves the customer’s billing outcome open.

**Play a round with a partner.** Share a resolved defect’s requirement and first symptom. Keep the diagnosis hidden. Each player chooses one check and predicts what two possible results would mean. Reveal the evidence one piece at a time. At the end, compare which check narrowed the investigation most and add it to the guide for the next learner.

### Learning through uncertainty

During a layoff or a difficult hiring cycle, a few quiet minutes of mindfulness or meditation can help you choose a manageable task. Trace one failed check, ask a colleague to review your reasoning, or take time to rest. This community makes room to give and receive that support.

## Make a move for the next learner

<img src="assets/aiqq-logo.webp" width="112" alt="AI Quality Queens crown logo">

Contributors at every level are welcome. A beginner who spots a missing setup step can make a tutorial easier for everyone who follows. An experienced tester can contribute a realistic failure case, a domain exercise, or an agent trace that shows how a plausible answer went wrong.

[Open an issue](https://github.com/kavitaj11/ai-quality-queens/issues) to ask a question, report a confusing step, or propose a lesson. To contribute a change, fork the repository, update the relevant guide or lab, and [open a pull request](https://github.com/kavitaj11/ai-quality-queens/compare). Tell reviewers who the change helps, what you tried, and what another learner should observe.

[Read the contribution guidelines](CONTRIBUTING.md) for lesson structure, verification, and pull request expectations.

## Citation

Kavita Jadhav. (2026). kavitaj11/ai-quality-queens: AI Quality Queens v1.0.0 — QA Automation Mastery System (Version v1.0.0) [Computer software]. Zenodo. [https://doi.org/10.5281/zenodo.24384800](https://doi.org/10.5281/zenodo.24384800)

## License

The code examples use the MIT License. The training materials use Creative Commons Attribution 4.0 International (CC BY 4.0). See the [license and attribution guide](LICENSE).
