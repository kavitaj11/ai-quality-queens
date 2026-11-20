# Phase overview: where AI plugs into QA

[14 · Module topics](../../modules/14-ai-augmented-qa.md) · [Curriculum index](../../README.md)

**Category** · OVERVIEW

One map for the whole phase. AI attacks the four expensive parts of automation: AUTHORING (LLMs draft tests), MAINTENANCE (self-healing locators), TRIAGE (flake clustering + LLM root-cause), and EXPLORATION (agentic loops) — with MCP as the plug that lets agents use your real tools. The rule that spans every lesson: AI drafts, heals, clusters, and explores; YOU review, gate, and own the quality bar.

## Worked reference

````text
AI-AUGMENTED QA — PHASE MAP

WHERE AI PLUGS INTO THE AUTOMATION LOOP

            ┌────────────────────────────────────┐
            │            YOUR TEST SUITE          │
            └────────────────────────────────────┘
   AUTHORING           MAINTENANCE          TRIAGE
   LLM test gen        self-healing         flake detection
   draft → review      locators: heal →     retry → cluster →
   → quarantine        log → fix in code    LLM root-cause
        │                    │                   │
        └──────────┬─────────┴──────────┬────────┘
                   ▼                    ▼
   EXPLORATION                    INTEGRATION
   agentic QA loops               MCP servers
   observe → decide → act         agents ⇄ your browser,
   files bugs as it goes          suites, data factories

MODULE MAP
  1  Self-Healing & AI Platforms
       heal mechanics · Healenium · Testim / mabl /
       Applitools — and how to evaluate them
  2  LLMs in QA
       prompt-with-context test generation, you as reviewer
  3  Agentic QA, Flake AI & MCP
       generation pipeline · flake triage · fallback
       healing · exploratory agents · MCP integration

THE ONE RULE THAT SPANS EVERYTHING
  AI output is a DRAFT — reviewed like a PR, quarantined
  until trusted, and logged when it acts on its own.
  A test that can't fail is worse than no test.
````

## Exercise

Before starting, rank the four cost centers (authoring, maintenance, triage, exploration) by pain in your current suite — study the modules in that order.
