# Cloud environment types: IaaS, PaaS, serverless

[10 · Module topics](../../modules/10-ci-cd-infrastructure.md) · [Curriculum index](../../README.md)

**Category** · CLOUD ENVIRONMENTS

Where your app — and your tests — actually run. IaaS (EC2/VMs): you own the OS up. PaaS/containers (ECS, EKS, App Service, Cloud Run): you ship images. Serverless (Lambda, Cloud Functions): you ship functions, the platform owns everything else. Each shifts what QA must test: the higher the abstraction, the less infra to test and the more config, limits, and cold starts to test.

## Worked reference

````text
CLOUD ENVIRONMENT TYPES — what runs where

              AWS          AZURE          GCP
  IaaS (VMs)  EC2          Virtual        Compute
              you own OS→  Machines       Engine
  Containers  ECS / EKS    AKS / App      GKE /
              you ship     Service        Cloud Run
              images
  Serverless  Lambda       Functions      Cloud
              you ship     ·              Functions
              functions
  + the services around them testers meet daily:
    S3/Blob/GCS (files) · RDS/SQL (DBs) · SQS/Service
    Bus/PubSub (queues) · CloudFront/CDN (caching)

WHAT QA TESTS AT EACH LEVEL
  IaaS        + OS patching, disk full, service restart
  containers  + image builds, orchestration, scaling
              policies, resource limits (OOM kills)
  serverless  + COLD STARTS (first-hit latency),
              timeouts (Lambda max 15 min), payload
              size limits, concurrency throttling

THE SHARED RESPONSIBILITY LINE
  provider tests the cloud; YOU test what's IN it —
  your config, your quotas, your IAM, your app.
  'It's managed' never means 'it's tested.'

ENVIRONMENT PARITY — the classic cloud bug
  staging on t3.small, prod on m5.xlarge → perf
  results don't transfer; document instance types
  as part of the test environment definition.
````

## Exercise

For one app you know, name its environment type on AWS, Azure, and GCP equivalents — then list which failure modes QA owns at that abstraction level.
