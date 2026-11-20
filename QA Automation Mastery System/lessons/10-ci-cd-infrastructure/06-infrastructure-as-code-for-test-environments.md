# Infrastructure as Code for test environments

[10 · Module topics](../../modules/10-ci-cd-infrastructure.md) · [Curriculum index](../../README.md)

**Category** · CLOUD INFRASTRUCTURE

Terraform, CloudFormation, CDK, and Pulumi turn environments into reviewable code — the mechanism behind the ephemeral per-PR envs from the Cloud Environments module in this phase. For QA the IaC skills that matter: reading a plan diff to see what an env change does, parameterizing envs (size, region, TTL), and testing the IaC itself — does apply converge, does destroy leave nothing, does the env match prod's shape?

## Worked reference

````text
IaC FOR TEST ENVIRONMENTS — envs as reviewable code

  THE TOOLS
  Terraform    declarative HCL, any cloud — the
               lingua franca
  CloudFormation/CDK   AWS-native (CDK = real code)
  Pulumi       IaC in Python/TS — tests in the same
               language as your suites

  THE LOOP
  write .tf → terraform plan (THE DIFF — read it
  like a PR) → apply → env exists → destroy

  WHAT QA READS IN A PLAN
  ~ modify   in-place change (instance size — perf
             baselines now invalid!)
  -/+ replace DESTROY then create — data loss in
             that resource; where did the DB go?
  + create / - destroy  scope of the env

  PARAMETERIZED ENVS (the QA interface)
  module "test_env" {
    size   = "pr"      # pr | perf | prod-mirror
    ttl    = "4h"      # reaper enforces
    region = var.region
  }
  one module, three shapes — PR envs small, perf
  envs PROD-SHAPED (or numbers don't transfer)

  TESTING THE IaC ITSELF
  • terraform validate + plan in CI on every change
  • apply → smoke-probe endpoints → destroy →
    verify NOTHING left (orphan scan)
  • drift detection: nightly plan against live —
    manual console edits show up as diffs
  • policy checks (OPA/Sentinel): no public
    buckets, TTL tags mandatory

  QA'S STAKE
  the env definition is part of the test system:
  an unreviewed infra change that shrinks staging
  invalidates every result run on it.
````

## Exercise

Read a Terraform plan for your staging env (or a sample). List: what would change, what would be destroyed, and which change could invalidate perf baselines.
