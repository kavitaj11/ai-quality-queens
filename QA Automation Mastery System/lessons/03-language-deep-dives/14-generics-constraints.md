# Generics & constraints

[03 · Module topics](../../modules/03-language-deep-dives.md) · [Curriculum index](../../README.md)

**Category** · TYPESCRIPT DEEP DIVE

Generics are types with a parameter — write one reusable, type-safe helper instead of one per type. <T> flows the caller's type through the function so the return is correctly typed. Constraints (T extends …) restrict what T can be. This is how typed API clients and fixture builders stay DRY without losing safety.

## Worked reference

````text
// Generic function: T flows from call site to return type
async function getJson<T>(url: string): Promise<T> {
  const r = await fetch(url);
  return r.json() as Promise<T>;
}
const user = await getJson<User>('/api/me');
user.email;                        // typed & autocompleted ✓

// Constraint: K must be a key of T
function pick<T, K extends keyof T>(obj: T, key: K): T[K] {
  return obj[key];
}
pick(user, 'email');               // ✓ returns string
// pick(user, 'phone');            // ❌ compile error

// Generic class: a typed fixture builder
class Builder<T> {
  constructor(private base: T) {}
  with(patch: Partial<T>): T {
    return { ...this.base, ...patch };
  }
}
````

## Exercise

Write a typed getJson<T>(url) helper and a pick<T, K extends keyof T>() utility; watch autocomplete follow the types.
