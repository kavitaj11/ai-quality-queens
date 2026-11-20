# Standards & certifications landscape

[00 · Module topics](../../modules/00-sdlc-test-engineering.md) · [Curriculum index](../../README.md)

**Category** · QE PROCESS QUALITY

ISTQB, ISO/IEC 29119, and IEEE 829 — what each is, who actually follows them, and when they matter. ISTQB certifies people and defined the industry's shared vocabulary; ISO 29119 standardizes process (and is controversial); IEEE 829 gave us the test-plan outline. Regulated industries audit against standards; most product companies run on engineering consensus instead — but the vocabulary is interview canon everywhere.

## Worked reference

````text
TESTING STANDARDS — who follows what, honestly

  ISTQB (certification body, since 2002)
    what   Foundation/Advanced/Expert certs + the
           GLOSSARY the industry speaks (test levels,
           severity vs priority, entry/exit criteria)
    where  consultancies · banks · government ·
           strong in Europe/India; optional in most
           product/tech companies
    value  shared vocabulary + interview signal;
           NOT proof you can automate

  ISO/IEC 29119 (process standard, 5 parts)
    what   definitions, processes, documentation,
           techniques for 'standardized' testing
    where  regulated: medical, aerospace, defense,
           finance — where audits demand evidence
    note   CONTROVERSIAL: context-driven community
           formally opposed it as too process-heavy —
           know both sides for senior interviews

  IEEE 829 (test documentation, now folded into 29119)
    what   the classic test-plan outline (scope, items,
           criteria, risks — see Strategy vs Plan)
    where  its skeleton survives in most orgs' plan
           templates, credited or not

  DOMAIN-SPECIFIC (non-negotiable where they apply)
    FDA / IEC 62304  medical software
    DO-178C          avionics
    ISO 26262        automotive
    PCI-DSS / SOC 2  payments / SaaS security evidence

WHAT'S ACTUALLY UNIVERSAL
  no standard body — engineering consensus: the test
  pyramid, CI gates, shift-left, risk-based priority,
  code-reviewed test code. Standards formalize
  vocabulary; consensus governs practice.

RULE OF THUMB
  regulated org  → standards are audited: follow them
  product org    → speak ISTQB, practice consensus
  interviews     → the vocabulary is canon everywhere
````

## Exercise

For your target employer type (startup, bank, medical device firm), decide: which standard would be enforced, which merely referenced, and whether an ISTQB cert changes your hiring odds there.
