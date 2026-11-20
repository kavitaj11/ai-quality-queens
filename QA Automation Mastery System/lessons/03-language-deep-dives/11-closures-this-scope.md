# Closures, this & scope

[03 · Module topics](../../modules/03-language-deep-dives.md) · [Curriculum index](../../README.md)

**Category** · JAVASCRIPT DEEP DIVE

A closure is a function bundled with the variables it captured — the basis of private state, factories, and callbacks. `this` is JavaScript's famous footgun: it depends on how a function is CALLED, not where it's defined. Arrow functions fix `this` to their lexical scope, which is why they're preferred for callbacks and page-object methods.

## Worked reference

````text
// Closure: 'count' is private, survives between calls
function makeCounter() {
  let count = 0;                 // not accessible outside
  return {
    inc() { return ++count; },
    value() { return count; },
  };
}
const c = makeCounter();
c.inc(); c.inc();                // c.value() === 2

// 'this' depends on HOW a function is called:
const page = {
  url: '/home',
  open() { console.log(this.url); },
};
page.open();                     // '/home'  ✓
const fn = page.open;
fn();                            // undefined ❌ (this lost)

// Arrow functions capture 'this' lexically — safe in callbacks:
class LoginPage {
  constructor(p) { this.p = p; }
  async submit() {
    // arrow keeps 'this' = the LoginPage instance
    await this.p.locator('button').click();
  }
}
````

## Exercise

Build a counter factory using a closure for private state, then break a method by extracting it and fix it with an arrow function or .bind().
