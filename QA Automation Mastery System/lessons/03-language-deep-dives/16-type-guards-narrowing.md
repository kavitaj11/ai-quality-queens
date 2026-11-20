# Type guards & narrowing

[03 · Module topics](../../modules/03-language-deep-dives.md) · [Curriculum index](../../README.md)

**Category** · TYPESCRIPT DEEP DIVE

Narrowing is TypeScript following your control flow to shrink a type: after typeof, instanceof, in, or an equality check, TS knows the more specific type inside that branch. Custom type guards (x is T) teach the compiler your own runtime checks — essential when API data enters your tests as unknown.

## Worked reference

````text
// Built-in narrowing: TS tracks the type per branch
function format(v: string | number) {
  if (typeof v === 'number') {
    return v.toFixed(2);      // v is number here
  }
  return v.trim();            // v is string here
}

// 'in' and 'instanceof' narrow too
function area(s: { r: number } | { w: number; h: number }) {
  return 'r' in s ? Math.PI * s.r ** 2 : s.w * s.h;
}

// Custom type guard: 'x is User' teaches the compiler
function isUser(x: unknown): x is User {
  return typeof x === 'object' && x !== null &&
         'email' in x && 'role' in x;
}

const data: unknown = await res.json();
if (isUser(data)) {
  data.email;                 // ✓ unlocked as User
} else {
  throw new Error('bad payload');
}
````

## Exercise

Write an isUser(x): x is User guard, validate an unknown API payload with it, and let TS unlock the typed fields inside the branch.
