# Financial trading: mock Q&A (1–10)

[17 · Module topics](../../modules/17-interview-prep.md) · [Curriculum index](../../README.md)

**Category** · DOMAIN SPECIFIC INTERVIEW PREP

The ten questions a hiring manager for this role opens with — framework from scratch, what to automate, API approach, reusable framework layers, tools, maintainability, environment assessment, metrics, microservices, flaky tests — each with a strong answer and the red flags that expose thin experience.

## Worked reference

````text
MOCK Q&A — QUESTIONS 1–10

Q1  Tell me about an automation framework you built from scratch.
STRONG ANSWER
  "I inherited an environment where API testing was largely manual
   and release validation required several days of effort.
   I performed an assessment of the testing landscape, identified
   high-risk services, and built a reusable API automation framework
   using Rest Assured, Java, TestNG, Maven, and Jenkins.
   The framework separated test data, configuration, and test logic
   to improve maintainability. I implemented reusable request
   builders, response validators, authentication modules, reporting,
   and environment management.
   Within six months we automated over 75% of regression coverage
   and reduced release validation time from three days to under
   four hours."
RED FLAGS
  "My team built it" · cannot explain architecture · cannot explain
  folder structure · cannot explain reporting strategy

Q2  How do you determine what should be automated?
  "I prioritize based on business risk, frequency of execution,
   regression value, and stability.
   I typically automate:
     Critical customer workflows · High-volume APIs
     Core backend services · Integration points
     Regulatory and financial calculations
   I avoid automating highly volatile features until requirements
   stabilize."

Q3  Walk me through your API testing approach.
  "For every API I validate:
     Status codes · Response schema · Business rules ·
     Data accuracy · Security · Error handling ·
     Performance thresholds
   I create positive, negative, boundary, and security scenarios.
   For example, when testing a financial transaction API I verify:
     Correct account updates · Ledger accuracy ·
     Duplicate prevention · Failure recovery · Audit logging"

Q4  How would you build a reusable API automation framework?
  CORE LAYER        authentication · environment configuration ·
                    logging · reporting
  SERVICE LAYER     API clients · request builders
  VALIDATION LAYER  assertions · business rule validations
  DATA LAYER        test data management · environment data
  EXECUTION LAYER   CI/CD integration
  "This allows new tests to be created quickly without duplicating
   code."

Q5  What tools have you used for API automation?
  Postman · ReadyAPI · Rest Assured · Swagger/OpenAPI · Jenkins ·
  GitHub Actions · JMeter · SQL validation
  "I typically use Postman for exploration and automation
   development and move larger suites into code-based frameworks
   for scalability."

Q6  How do you ensure test automation remains maintainable?
  Reusable components · centralized configuration · version control ·
  coding standards · documentation · peer reviews
  "I also avoid hard-coded test data and build frameworks so junior
   engineers can contribute without extensive onboarding."

Q7  How would you evaluate our current testing environment?
  First 30 days = assessment. Review: existing automation coverage ·
  regression execution times · CI/CD integration · defect trends ·
  framework architecture · test maintenance costs.
  "I would then identify gaps and create a prioritized roadmap based
   on business risk and ROI."

Q8  What metrics do you track?
  Automation coverage · defect escape rate · regression execution
  time · test reliability · production incidents · mean time to
  detect defects · mean time to resolve issues.
  "The goal is improving release confidence while reducing manual
   effort."

Q9  Describe your experience testing microservices.
  Service-level testing · contract testing · integration testing ·
  data validation · event testing · end-to-end workflow validation.
  "I validate both individual service behavior and downstream
   impacts across the ecosystem."

Q10 How do you handle a flaky automated test?
  "First I determine whether the issue originates from: test design ·
   environment instability · data dependencies · timing issues ·
   application defects.
   I never ignore flaky tests because they reduce trust in the
   automation suite. I treat them as production defects and resolve
   them immediately."
````

## Exercise

Answer all ten out loud from memory with your own project names and numbers substituted in.
