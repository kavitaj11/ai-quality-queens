# Probability & statistics for QA

[04 · Module topics](../../modules/04-computational-logic.md) · [Curriculum index](../../README.md)

**Category** · MATH & LOGIC FOR TESTERS

Flakiness, sampling, and performance all speak statistics. A 2%-flaky test in a 200-test suite means almost every run fails somewhere: 1-(0.98)²⁰⁰ ≈ 98%. Percentiles (p50/p95/p99) describe latency honestly where averages lie. And any 'is it really faster?' claim needs enough samples to beat the noise.

## Worked reference

````text
import statistics as st

# Flakiness compounds across a suite
p_flake, n_tests = 0.02, 200
p_clean = (1 - p_flake) ** n_tests
print(f"P(all green) = {p_clean:.1%}")      # ~1.8%

# Percentiles, not means
lat = sorted(samples)
q = st.quantiles(lat, n=100)
p50, p95, p99 = q[49], q[94], q[98]
assert p95 < 800, f"p95 {p95}ms exceeds budget"

# Expected runs until first failure of a 1-in-50 flake
print(1 / 0.02)                              # 50 runs
````

## Exercise

A test fails 1 run in 50. With retries=2, what's the chance it still gates a build? How many suite runs before you'd expect to see it fail at least once?
