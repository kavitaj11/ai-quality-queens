# Debugging network failures

[05 · Module topics](../../modules/05-networking.md) · [Curriculum index](../../README.md)

**Category** · TOOLING & DEBUGGING

A taxonomy plus a drill. Timeout, refused, reset, DNS failure, TLS error, 502/503/504 — each points at a different culprit. The drill: reproduce with curl -v (isolates app vs environment), read which phase died, then walk the path client → DNS → LB → service. Gateway status codes are the LB narrating exactly this walk for you.

## Worked reference

````text
NETWORK FAILURE TAXONOMY — read the error, name the culprit

  SYMPTOM                  USUAL CULPRIT
  DNS failure              name wrong · env not in this
  (NXDOMAIN)               network's DNS · VPN needed
  connection refused       service down or wrong port —
                           fails FAST
  connection timeout       firewall silently drops, or
                           host unreachable — fails SLOW
  connection reset         peer/LB killed the socket:
                           crash, idle timeout, deploy
  TLS errors               see TLS lesson (CA, SAN, expiry)
  502 bad gateway          LB reached a DEAD/crashing
                           backend
  503 unavailable          no healthy backends (deploy
                           in progress? health checks red?)
  504 gateway timeout      backend alive but TOO SLOW
                           for the LB's patience
  hangs forever            missing client timeout — YOUR
                           bug: every test client sets one

THE DRILL
  1. curl -v the same call from the test runner —
     app problem or environment problem?
  2. read WHICH phase died: resolve → connect →
     TLS → sent → response (maps to the taxonomy)
  3. walk the path: client → DNS → LB → service;
     gateway codes tell you how far you got
  4. intermittent? correlate timestamps with deploys,
     scaling events, LB idle timeouts (the 60s classic)

CI FLAKE ANGLE
  'flaky test' that's really infra: resets during
  deploys · DNS inside containers · port collisions
  in parallel runs · ephemeral env not warm yet —
  tag infra failures separately from product failures
  or your flake metrics blame the wrong thing.
````

## Exercise

Collect 5 real failures from your test runs and classify each: which layer, which curl phase would show it, and what single question would you ask the platform team.
