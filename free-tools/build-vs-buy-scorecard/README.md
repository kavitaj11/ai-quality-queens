# Build vs. Buy Scorecard

A free spreadsheet for deciding whether to build a capability in-house or buy it from a vendor, such as single sign-on, a test management platform, an evaluation service or an AI model.

[Download the scorecard (.xlsx)](Build-vs-Buy-Scorecard.xlsx)

## What it answers

Which option scores better on the criteria that matter to your team, and how much of that score rests on evidence rather than assumption?

## How to use it

1. Open the **Scorecard** sheet and adjust the weights so they total 100%.
2. Score Build and Buy from 1 to 5 for each of the ten criteria. A 5 is better for that option. For risk criteria, 5 means the lowest risk.
3. Tag every score as **Evidence** (you checked it and can name the source) or **Assumption** (you believe it but have not checked).
4. Read the results under the table: which way the scores lean, by how much, and how much weight still rests on assumptions.

Yellow cells with blue text are inputs. Black cells are formulas.

## A tester's reading of the result

Treat each Assumption like an untested requirement. Before the decision is final, pick the highest-weight assumption and design the check that would turn it into evidence: a time-boxed spike, a vendor's written answer, a load test or a call with a reference customer. If the result is "Too close to call", those checks decide it.

## The full kit

The full Build vs. Buy Decision Kit from Karmic Trilogy AI Lab adds a 3-year cost model, a vendor-risk checklist, an exit plan, an architecture decision record and a worked single sign-on example. It is licensed separately from this repository.

## License

This scorecard is a training material and is licensed under [CC BY 4.0](../../LICENSES/CC-BY-4.0.txt), like the rest of AI Quality Queens. The full kit is not covered by this license.

The scorecard was tested in Microsoft Excel. Other spreadsheet apps should open it, but check that the dropdowns and formulas behave as expected.
