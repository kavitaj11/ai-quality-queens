# Financial trading: regression, CI/CD & 30-60-90

[17 · Module topics](../../modules/17-interview-prep.md) · [Curriculum index](../../README.md)

**Category** · DOMAIN SPECIFIC INTERVIEW PREP

Questions 7, 10, and 11: a five-phase plan to convert 250 manual API cases into an automated regression suite, which suite runs at which pipeline stage, and the first-30/60/90-day plan you present in five minutes. Plus the scoring rubric the interviewer marks you against.

## Worked reference

````text
Q7  BUILD A REGRESSION PLAN

THE SITUATION
  250 manually executed API test cases · three-day regression cycles ·
  biweekly releases · multiple backend integrations · frequent
  test-environment instability · limited automation experience on the
  existing QA team.

PHASE 1 — ASSESSMENT
  Review existing test cases and defect history · identify critical
  business services · analyze production incidents · identify
  repeated manual tests · review environments, pipelines, and test
  data · remove obsolete and duplicate tests · establish baseline
  quality metrics.

PHASE 2 — PRIORITIZATION (automate first)
  Critical financial workflows · high-volume APIs · smoke and
  deployment-validation scenarios · stable, repeatable regression
  cases · authentication and authorization · data integrity and
  reconciliation · historical high-defect areas.

PHASE 3 — FRAMEWORK PILOT
  Create core reusable framework components · automate 10 to 20
  representative scenarios · prove pipeline integration · validate
  reporting and debugging capabilities · gather team feedback before
  scaling.

PHASE 4 — SCALE THE REGRESSION SUITE
  Organize tests by service and risk · add test tags · run smoke
  tests per deployment · run targeted service tests per code change ·
  run broader regression nightly · quarantine unstable tests with
  clear ownership · track flaky-test trends.

PHASE 5 — TEAM ADOPTION
  Pair with existing QA engineers · conduct code reviews · create
  framework documentation · provide templates and examples · conduct
  knowledge-transfer sessions · identify internal framework owners.

Q10 PIPELINE STRATEGY — when each suite runs
  PULL REQUEST        static analysis · framework unit tests ·
                      contract checks · small API smoke suite ·
                      tests for the services affected by the change
  DEPLOY TO QA        environment health checks · authentication
                      validation · deployment smoke tests · critical
                      account and transfer workflows
  NIGHTLY             broader API regression · integration tests ·
                      data reconciliation · negative scenarios ·
                      cross-service workflows
  RELEASE CANDIDATE   full critical regression · end-to-end
                      validation · performance checks ·
                      security-focused scenarios · financial
                      reconciliation · audit and compliance
                      validation
  PRODUCTION          non-destructive health checks · synthetic
                      monitoring · basic service availability ·
                      response-time monitoring · error-rate and
                      business-event monitoring

Q11 FIRST 30, 60, AND 90 DAYS  (five-minute presentation)
  FIRST 30 DAYS
    Assess current testing landscape · review tools, APIs,
    architecture, and pipelines · analyze defects and production
    issues · identify critical regression scenarios · define
    framework standards · establish baseline metrics.
  DAYS 31–60
    Build the core automation framework · automate priority smoke and
    API regression tests · integrate tests into CI/CD · implement
    logging and reporting · conduct framework demonstrations · begin
    team pairing and training.
  DAYS 61–90
    Expand regression coverage · reduce flaky tests · implement
    quality gates · document framework architecture and standards ·
    transition ownership to permanent team members · report
    improvements in regression time and release quality.

SCORING RUBRIC (100 points)
  1 API TEST DESIGN                                          15
      positive/negative/boundary 5 · business-rule and financial
      validation 5 · security, idempotency, failure recovery 5
  2 HANDS-ON AUTOMATION                                      20
      reusable, readable implementation 5 · response and schema
      validation 5 · backend data validation 5 ·
      duplicate-processing prevention 5
  3 FRAMEWORK ARCHITECTURE                                   15
      separation of concerns 5 · configuration, data,
      authentication, reporting 5 · maintainability 5
  4 BACKEND AND SQL VALIDATION                               10
      accurate queries 5 · data integrity / DB-testing risks 5
  5 REGRESSION AND CI/CD STRATEGY                            15
      risk-based automation plan 5 · pipeline execution strategy 5 ·
      metrics and continuous improvement 5
  6 DEBUGGING AND TROUBLESHOOTING                            10
      identifies technical problems 5 · practical corrections 5
  7 FINANCIAL SERVICES QUALITY MINDSET                       10
      precision and reconciliation 3 · idempotency and transactional
      integrity 4 · auditability, security, recovery 3
  8 LEADERSHIP AND HANDOFF                                    5
      team adoption 2 · documentation and knowledge transfer 3

STRONG CLOSING ANSWER
  "My approach would be to first understand the highest-risk
   financial workflows and the current testing bottlenecks. I would
   then build a reusable API automation foundation, prove it through
   a focused pilot, and integrate the tests into the delivery
   pipeline. I would remain hands-on throughout the implementation,
   but I would also prioritize documentation, pairing, code reviews,
   and knowledge transfer so the internal team can maintain and
   expand the solution after my engagement ends."

WHAT THIS TEST IS REALLY EVALUATING
  Not whether you know Postman. The strongest candidate demonstrates:
  hands-on framework development · strong API and backend testing
  knowledge · financial data accuracy and reconciliation ·
  idempotency and duplicate-prevention expertise · practical
  regression prioritization · CI/CD integration · troubleshooting
  across distributed services · sustainable framework design ·
  QA leadership without becoming purely architectural.
````

## Exercise

Present the 30-60-90 plan in five minutes to a timer, with one measurable outcome per phase.
