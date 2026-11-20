# Kubernetes for test workloads

[10 · Module topics](../../modules/10-ci-cd-infrastructure.md) · [Curriculum index](../../README.md)

**Category** · CLOUD INFRASTRUCTURE

Where suites actually run at scale: tests as Jobs (run-to-completion), browser pods instead of VMs, namespaces as disposable test environments, and resource requests/limits that stop the OOM-killed-Chrome class of flake. You don't need to operate a cluster — you need to read one: why did my pod evict, why is the browser slow, why did DNS fail between services.

## Worked reference

````text
KUBERNETES FOR TEST WORKLOADS — read the cluster

  THE SHAPES
  Job          run-to-completion — a suite shard
               (parallelism: 8 = sharding built in)
  Deployment   long-running — Selenium Grid /
               Moon browser pool, report server
  Namespace    a disposable TEST ENV: app + deps
               isolated per PR; delete namespace =
               teardown (the k8s ephemeral env)
  CronJob      nightly full matrix

  RESOURCES — the flake source nobody checks
  requests: what the pod is promised
  limits:   where it's killed
  browsers are hungry: chrome pod under 2Gi limit →
  OOMKilled mid-test → 'browser crashed' flake
  CPU throttling → slow renders → timeout flakes
  (dedicate shm: emptyDir medium=Memory for chrome)

  DEBUGGING VOCABULARY (enough to file real tickets)
  kubectl get pods -n test-pr-421     what's running
  kubectl describe pod X              WHY pending/
                                      evicted (events!)
  kubectl logs X --previous           the crash before
                                      the restart
  OOMKilled · Evicted · CrashLoopBackOff · Pending
  (no capacity) — each is a different ticket

  CLUSTER-SHAPED FLAKES
  • pod eviction mid-run (node pressure) → retries
    absorb, but TAG as infra (see flake attribution)
  • DNS between services is per-namespace — 'db not
    found' = wrong namespace, not missing db
  • image pull on every run → 5 min 'queued' — pin +
    pre-pull images
  • noisy neighbors: perf tests need dedicated
    node pools or numbers lie
````

## Exercise

Take one flaky-in-CI-only test and investigate the k8s angle: pod resources, evictions, DNS. Write the kubectl commands you'd run and what each would tell you.
