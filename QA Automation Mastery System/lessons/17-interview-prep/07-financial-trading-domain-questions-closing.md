# Financial trading: domain questions & closing

[17 · Module topics](../../modules/17-interview-prep.md) · [Curriculum index](../../README.md)

**Category** · DOMAIN SPECIFIC INTERVIEW PREP

The financial-services-specific questions (testing a stock trade API, testing financial calculations), the drill-down questions that expose fake experience, the smart questions to ask them, and the 30-second close.

## Worked reference

````text
FINANCIAL SERVICES SPECIFIC QUESTIONS (VERY LIKELY)

How would you test a stock trade API?
  "I would validate:
     Trade creation · Order execution · Settlement processing ·
     Account balance updates · Position updates ·
     Duplicate prevention · Market validation rules ·
     Security permissions · Audit trails
   In financial services, accuracy is more important than UI
   functionality because a single incorrect trade can have
   significant business impact."

How do you test financial calculations?
  "I validate calculations against expected business rules using
   known datasets and SQL verification.
   I focus heavily on: Precision · Rounding rules · Interest
   calculations · Transaction fees · Balance updates ·
   Historical consistency"

QUESTIONS THAT EXPOSE FAKE EXPERIENCE
A Schwab manager may drill deeply.

What does your framework folder structure look like?
  src
   ├── api
   ├── tests
   ├── utilities
   ├── config
   ├── data
   ├── reports
   └── runners

Explain how authentication is handled in your API framework.
  "Most commonly OAuth2, JWT, Bearer Tokens, or client credentials.
   I centralize token generation and reuse it across test
   execution."

How do you validate responses?
  JSON Schema validation · assertions · database verification ·
  business rules validation

SMART QUESTIONS TO ASK SCHWAB
  • What percentage of testing is currently manual versus automated?
  • What API automation tools are currently being used?
  • What is causing the biggest quality bottleneck today?
  • Do you have an existing framework that needs improvement or will
    this be greenfield?
  • How is automation integrated into your CI/CD pipeline?
  • What would success look like after the first 90 days?
  • What are the most critical customer-facing services this role
    would support?

30-SECOND CLOSING STATEMENT
  "I believe my background aligns well because I've repeatedly built
   scalable API and backend automation frameworks from the ground
   up, automated large regression suites, and improved release
   quality through sustainable QE practices. I'm hands-on,
   comfortable leading automation initiatives, and focused on
   leaving teams with frameworks and processes they can successfully
   maintain long term. I would be excited to bring that experience
   to Charles Schwab and help strengthen both automation maturity
   and overall software quality."
````

## Exercise

Draft your own answer to “how would you test a stock trade API?” in under 90 seconds of speech.
