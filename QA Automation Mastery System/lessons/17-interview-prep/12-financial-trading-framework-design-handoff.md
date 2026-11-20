# Financial trading: framework design & handoff

[17 · Module topics](../../modules/17-interview-prep.md) · [Curriculum index](../../README.md)

**Category** · DOMAIN SPECIFIC INTERVIEW PREP

Question 4 of the assessment: a scalable framework for account, transfer, and financial transaction services — components, folder structure, test data, authentication, config, reporting, logging, DB validation, parallel execution, CI/CD, and the handoff that makes it survive you.

## Worked reference

````text
Q4  DESIGN THE AUTOMATION FRAMEWORK

Explain: major components · folder structure · test-data strategy ·
authentication handling · environment configuration · reporting ·
logging · database validation · parallel execution · CI/CD
integration · documentation and team handoff.

STRONG FRAMEWORK DESIGN
  automation-framework/
  │
  ├── src/
  │   ├── main/
  │   │   ├── clients/
  │   │   │   ├── AccountClient.java
  │   │   │   └── TransferClient.java
  │   │   ├── models/
  │   │   │   ├── TransferRequest.java
  │   │   │   └── TransferResponse.java
  │   │   ├── auth/
  │   │   │   └── TokenProvider.java
  │   │   ├── config/
  │   │   │   └── ConfigurationManager.java
  │   │   ├── database/
  │   │   │   └── TransactionRepository.java
  │   │   ├── assertions/
  │   │   │   └── FinancialAssertions.java
  │   │   └── utilities/
  │   │       ├── TestDataFactory.java
  │   │       └── RetryUtility.java
  │   │
  │   └── test/
  │       ├── regression/
  │       ├── integration/
  │       ├── contract/
  │       ├── smoke/
  │       └── negative/
  │
  ├── resources/
  │   ├── schemas/
  │   ├── test-data/
  │   └── environment-config/
  │
  ├── reports/
  ├── pipeline/
  ├── documentation/
  ├── pom.xml
  └── README.md

STRONG EXPLANATION
  "I would separate service clients, test logic, data creation,
   configuration, authentication, and assertions. Tests should
   describe the business scenario rather than contain low-level
   request-building code.
   Authentication would be centralized through a token provider.
   Environment details would come from runtime variables or
   secret-management tools rather than being hard-coded.
   The framework would support test tagging so smoke tests can run
   on every pull request, integration tests can run in the
   deployment pipeline, and the full regression suite can run
   nightly or before a release.
   For maintainability, I would include a README, coding standards,
   test-writing examples, troubleshooting instructions, pipeline
   documentation, and knowledge-transfer sessions."

Q9  TEAM PUSHBACK
  "Automation takes too long. We can execute these tests faster
   manually."

  "I would acknowledge that automation has an initial investment and
   avoid trying to automate everything immediately. I would
   demonstrate value with a small pilot covering stable, high-risk
   regression scenarios.
   I would compare the one-time automation investment against
   repeated manual execution across every release. I would also show
   improvements in execution time, repeatability, defect detection,
   release confidence, and team capacity.
   The goal is not to remove thoughtful manual testing. It is to
   reduce repetitive validation so the team has more time for
   exploratory, integration, and risk-based testing."
````

## Exercise

Draw this structure on a whiteboard in five minutes, naming the trade-off behind every folder.
