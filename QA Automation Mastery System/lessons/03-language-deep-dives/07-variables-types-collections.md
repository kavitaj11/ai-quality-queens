# Variables, types & collections

[03 · Module topics](../../modules/03-language-deep-dives.md) · [Curriculum index](../../README.md)

**Category** · JAVASCRIPT DEEP DIVE

Start with the language basics. Declarations: const (default), let (reassignable), never var. JavaScript has 7 primitives (string, number, boolean, null, undefined, bigint, symbol) and everything else is an object. The collections you'll use in tests: arrays (ordered), objects (string keys), and the purpose-built Map (any-key lookups) and Set (unique values). typeof, truthiness, and === vs == round it out.

## Worked reference

````text
// ── DECLARATIONS: const by default, let if reassigned ──
const BASE = 'https://api.shop.dev';   // block-scoped, no rebind
let attempts = 0;                       // reassignable
// var  -> avoid: function-scoped, hoisted, bug-prone

// ── PRIMITIVES (7) ──
'checkout'        // string
42, 19.99, NaN    // number (one type for int+float)
true, false       // boolean
null              // intentional empty
undefined         // not assigned
10n               // bigint
Symbol('id')      // unique symbol
typeof 42;        // 'number'   (typeof null === 'object' — gotcha)

// ── ARRAY — ordered list ──
const emails = ['a@t.dev', 'b@t.dev'];
emails.push('c@t.dev');
emails[0]; emails.at(-1);               // first / last
emails.map(e => e.trim());
emails.filter(e => e.endsWith('.dev'));
emails.find(e => e.startsWith('a'));

// ── OBJECT — string-keyed record ──
const order = { id: 1, sku: 'A-100', qty: 2 };
order.status = 'pending';               // add
const { sku, qty } = order;             // destructure
order?.address?.city ?? 'n/a';          // safe access + default

// ── MAP — keys of ANY type, ordered, size ──
const byId = new Map();
byId.set(1, order);
byId.get(1); byId.has(1); byId.size;

// ── SET — unique values, fast membership ──
const seen = new Set(emails);
seen.add('a@t.dev');                    // no-op
seen.has('b@t.dev');

// ── reduce: tally statuses ──
const tally = results.reduce((acc, r) => {
  acc[r.status] = (acc[r.status] ?? 0) + 1; return acc;
}, {});

// == coerces (avoid), === compares strictly (use this)
0 == '';    // true  ❌   0 === '';  // false ✓
````

## Exercise

From an array of result objects, build a Map keyed by test name, a Set of suites that ran, and tally statuses with reduce. Then trip the == vs === gotcha deliberately.
