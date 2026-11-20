# The event loop & call stack

[03 · Module topics](../../modules/03-language-deep-dives.md) · [Curriculum index](../../README.md)

**Category** · JAVASCRIPT DEEP DIVE

JavaScript is single-threaded: one call stack, one thing at a time. Slow I/O doesn't block it because the event loop offloads timers, network, and I/O, then runs their callbacks when the stack is empty. Microtasks (promises) drain before macrotasks (setTimeout) — the source of many 'why did this log first?' puzzles.

## Worked reference

````text
// One call stack. The event loop feeds it from queues.
console.log('1: sync');

setTimeout(() => console.log('4: macrotask'), 0);

Promise.resolve().then(() => console.log('3: microtask'));

console.log('2: sync');

// Output: 1, 2, 3, 4
// Why: all sync code runs first (1, 2). Then the
// microtask queue drains fully (3) BEFORE any
// macrotask/timer callback (4).

// This is why a forgotten await doesn't crash — the
// promise resolves later, on a future loop tick,
// while your test has already raced ahead.
````

## Exercise

Predict the log order of a script mixing sync code, setTimeout(0), and Promise.resolve().then(). Run it and explain.
