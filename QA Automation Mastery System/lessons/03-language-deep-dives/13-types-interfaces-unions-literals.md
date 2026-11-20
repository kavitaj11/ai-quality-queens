# Types, interfaces, unions & literals

[03 · Module topics](../../modules/03-language-deep-dives.md) · [Curriculum index](../../README.md)

**Category** · TYPESCRIPT DEEP DIVE

TypeScript adds a compile-time type layer over JavaScript — caught by tsc before any browser opens. interface/type describe object shapes; union types (A | B) say 'one of these'; literal unions ('admin' | 'user') turn a string into a small, typo-proof set of legal values. This is your first quality gate.

## Worked reference

````text
// interface = object shape
interface User {
  id: string;
  email: string;
  role: 'admin' | 'customer';    // literal union: only 2 legal
}

// Union types: a value that is one of several shapes
type Id = string | number;

// Discriminated union — the 'kind' field narrows it
type ApiResult =
  | { kind: 'ok';    data: User }
  | { kind: 'error'; message: string };

function handle(res: ApiResult) {
  if (res.kind === 'ok') {
    return res.data.email;         // TS knows .data exists here
  }
  return res.message;              // and .message here
  // Forget a branch? tsc errors. Typo 'Admin'? tsc errors.
}
````

## Exercise

Model an API response with a discriminated union (success vs error) and let the compiler force you to handle both.
