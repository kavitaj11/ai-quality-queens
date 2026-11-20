# Promises: states, chaining & combinators

[03 · Module topics](../../modules/03-language-deep-dives.md) · [Curriculum index](../../README.md)

**Category** · JAVASCRIPT DEEP DIVE

A Promise is a value that arrives later — pending, then fulfilled or rejected, once and forever. .then returns a new promise (so they chain), .catch handles rejection, .finally always runs. Combinators matter: all (fail-fast), allSettled (never rejects), race, any. Every await is a promise underneath.

## Worked reference

````text
// A promise settles once: fulfilled OR rejected
const getUser = (id) =>
  fetch('/api/users/' + id).then((r) => {
    if (!r.ok) throw new Error('HTTP ' + r.status);
    return r.json();
  });

// Chaining: each .then hands its return to the next
getUser(1)
  .then((u) => getOrders(u.id))   // return a promise -> flattened
  .then((orders) => console.log(orders.length))
  .catch((err) => console.error('failed:', err))
  .finally(() => console.log('done'));

// Combinators — know the difference:
await Promise.all([a, b, c]);        // all succeed, or reject fast
await Promise.allSettled([a, b, c]); // never rejects; per-item result
await Promise.race([slow, timeout]); // first to settle wins
await Promise.any([a, b, c]);        // first to SUCCEED
````

## Exercise

Use Promise.allSettled to hit 5 endpoints and report which succeeded and which failed, without one failure aborting the rest.
