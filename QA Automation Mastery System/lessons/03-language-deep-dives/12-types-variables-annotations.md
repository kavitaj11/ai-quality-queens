# Types, variables & annotations

[03 · Module topics](../../modules/03-language-deep-dives.md) · [Curriculum index](../../README.md)

**Category** · TYPESCRIPT DEEP DIVE

TypeScript is JavaScript plus type annotations checked at compile time. Annotate variables (name: type), infer where obvious, and reach for the core types: primitives, arrays (T[]), tuples ([string, number]), object shapes, plus the TS-only any (avoid), unknown (safe any), void, never, and literal types. Typed collections — Array<T>, Map<K,V>, Set<T>, Record<K,V> — carry test data with autocomplete and no typos.

## Worked reference

````text
// ── ANNOTATIONS: name: type (or let TS infer) ──
const base: string = 'https://api.shop.dev';
let attempts = 0;                 // inferred as number
const retries: number = 3;
const ok: boolean = true;

// ── CORE TYPES ──
let anything: any;                // opts OUT of checking — avoid
let safe: unknown;                // like any, but must narrow first
function log(): void {}           // returns nothing
function fail(): never { throw new Error(); }  // never returns

// Arrays & tuples
const emails: string[] = ['a@t.dev'];
const pair: [string, number] = ['A-100', 2];   // fixed-shape tuple

// ── OBJECT SHAPES: interface vs type alias ──
interface Order { id: number; sku: string; qty: number }
type Status = 'pending' | 'shipped' | 'cancelled';  // literal union
const order: Order = { id: 1, sku: 'A-100', qty: 2 };

// ── TYPED COLLECTIONS ──
const byId = new Map<number, Order>();
byId.set(1, order);               // key must be number, value Order
const suites = new Set<string>(['checkout', 'login']);
const tally: Record<Status, number> =
  { pending: 3, shipped: 5, cancelled: 1 };

// Optional & readonly members
interface User { id: string; phone?: string; readonly role: Status }

// The payoff — typos and wrong values fail at compile time:
const s: Status = 'shiped';       // ❌ tsc error, caught pre-run
````

## Exercise

Type a test-result record, build a typed Map<string, Result> and a Record<Status, number> tally, and watch tsc reject a wrong status string.
