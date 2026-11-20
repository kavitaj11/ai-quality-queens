# Questions to ask the interviewer

> **Learning objective** · Use follow-up questions to understand how a team practices quality engineering.

## ◈ Ask for a recent example

“What does quality look like here?” often invites a broad answer. “Could you walk me through the last change that failed a pull-request check?” reveals who investigated, what evidence they had, and how the team decided whether the change could proceed.

| What you want to learn | Opening question | Follow-up |
|---|---|---|
| Ownership | How do developers, testers and product managers plan coverage? | Who decided which risks to cover in the last release? |
| Reliability | What happens when a browser test fails intermittently? | How does a quarantined test return to the required suite? |
| Communication | How do teams resolve a disagreement about release risk? | What evidence did they use the last time that happened? |
| Expectations | Which testing problem should this role address first? | What would improvement look like after three months? |

## Questions about how the team works

Choose two or three questions that matter to the role. Ask for a recent example and follow the evidence where it leads.

| Topic | Ask the interviewer | Follow up |
|---|---|---|
| Test strategy | Which recent change made the team reconsider where a test belonged? | What did you move to another layer, and how did you know it helped? |
| CI reliability | When a required check becomes flaky, who owns the investigation? | What evidence does the team require before restoring a quarantined check? |
| Shared environments | How do parallel jobs get independent test data? | What happens to records left behind by an interrupted run? |
| Service contracts | How does a provider team discover which consumers rely on a changed field? | Who follows up when a consumer cannot run its checks? |
| Accessibility | Where does hands-on accessibility testing fit into a feature release? | Can you describe a keyboard or screen-reader issue that changed a design? |
| Release authority | What happens when QA and product interpret the same release evidence differently? | Who records the decision and tracks the remaining risk? |
| Learning from incidents | Which escaped defect most recently changed the test strategy? | What check or review would catch the same failure today? |
| AI-assisted testing | How do reviewers decide whether to keep a generated test or a proposed locator repair? | Can you recall a suggestion the team rejected and why? |
| Agent oversight | Which actions may a QA agent take independently, and which require approval? | How do you verify what action the agent actually took? |
| Role expectations | What quality decision would you expect me to own in my first 90 days? | What evidence would show I had earned that responsibility? |

## ✎ Practice

Choose two questions for a ten-minute conversation with an engineering manager. If the manager gives a tester-to-developer ratio, follow up by asking how the team shares test design and failure investigation. The staffing figure provides context. The example tells you what the work involves.

---

[Return to the training guide](../README.md)
