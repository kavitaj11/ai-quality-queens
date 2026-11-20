# Distributions, mean & variance

[04 · Module topics](../../modules/04-computational-logic.md) · [Curriculum index](../../README.md)

**Category** · MATH FOR AI ENGINEERING

ML is statistics at scale: data comes from distributions, models learn their shape, and drift is the distribution moving under you. Mean locates the center, variance/std measure spread, z-scores flag outliers, and the normal curve's 68-95-99.7 rule turns 'looks weird' into a number. This is the math of data-quality tests, drift monitors, and anomaly detection.

## Worked reference

````text
import statistics as st

xs = [t["duration_ms"] for t in runs]
mu, sd = st.mean(xs), st.pstdev(xs)

# z-score outliers: beyond 3 std = 1-in-370 event
outliers = [t for t in runs
            if abs(t["duration_ms"] - mu) > 3 * sd]

# Drift gate: production inputs vs training baseline
base_mu, base_sd = baseline["len_mean"], baseline["len_std"]
prod_mu = st.mean(len(x) for x in prod_inputs)
assert abs(prod_mu - base_mu) < 2 * base_sd, \
    f"input-length drift: {base_mu:.0f} → {prod_mu:.0f}"

# Skew check — if mean >> median, don't use z-scores;
# switch to percentile-based gates
print(mu, st.median(xs))
````

## Exercise

Compute mean and std of your suite's test durations; flag every test beyond 3 std. Then argue: which flagged ones are real problems vs a heavy-tailed distribution being normal?
