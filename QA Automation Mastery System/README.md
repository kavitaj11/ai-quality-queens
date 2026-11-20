# QA Automation Mastery System

Follow the [18-week beginner planner and AI QA mastery track](planner.md) for a weekly sequence, or choose a module below for focused practice. The modules retain the artboard’s numbering and overlapping study windows. Each module links to its categorized lessons and a practice assignment. Browse all **269 topic entries**, or use the [19 review flashcards](flashcards.md) for recall practice.

| Module | Original artboard week(s) |
|---|---|
| [00 · SDLC & Test Engineering](modules/00-sdlc-test-engineering.md) | 5–6 |
| [01 · Foundations](modules/01-foundations.md) | 1–3 |
| [02 · Test Harness Setup](modules/02-test-harness-setup.md) | 3–4 |
| [03 · Language Deep Dives](modules/03-language-deep-dives.md) | 3–4 |
| [04 · Computational Logic](modules/04-computational-logic.md) | 3–4 |
| [05 · Networking](modules/05-networking.md) | 4 |
| [06 · Selenium](modules/06-selenium.md) | 4–8 |
| [07 · Playwright](modules/07-playwright.md) | 9–12 |
| [08 · Frameworks & BDD](modules/08-frameworks-bdd.md) | 13–15 |
| [09 · API Testing](modules/09-api-testing.md) | 16–18 |
| [10 · CI/CD & Infrastructure](modules/10-ci-cd-infrastructure.md) | 19–21 |
| [11 · Non-Functional Testing](modules/11-non-functional-testing.md) | 21–22 |
| [12 · Microservices Testing](modules/12-microservices-testing.md) | 22 |
| [13 · Test Automation System Design](modules/13-test-automation-system-design.md) | 22–23 |
| [14 · AI-Augmented QA](modules/14-ai-augmented-qa.md) | 23 |
| [15 · Autonomous QA Pipelines](modules/15-autonomous-qa-pipelines.md) | 23–24 |
| [16 · AI Testing & LLM Evaluation](modules/16-ai-testing-llm-evaluation.md) | 23–24 |
| [17 · Interview Prep](modules/17-interview-prep.md) | 24 |

The lesson pages include the default displayed reference or example. For alternate language tabs and interactive quizzes, the [interactive source artboard](https://claude.ai/artifact/HM3XUAEuQ29DSb9ckCWt6s#artboard-787b4c38acdc) provides the original interactive experience.

## Interactive dashboard and GitHub Pages

The [dashboard source](web/) builds a static site from this curriculum. It supports topic search, lesson completion, weekly planning, and flashcard practice. This browser stores your progress on this device.

To preview from the repository root with Python 3:

```sh
python3 "QA Automation Mastery System/web/build.py" _site
python3 -m http.server 8000 --directory _site
```

Open http://localhost:8000. To publish, choose **Settings → Pages → Build and deployment → GitHub Actions**. Run [Deploy QA Automation Mastery to Pages](https://github.com/kavitaj11/ai-quality-queens/actions/workflows/mastery-pages.yml). Later changes to this curriculum rebuild the site automatically. The deployment job shows the published URL.
